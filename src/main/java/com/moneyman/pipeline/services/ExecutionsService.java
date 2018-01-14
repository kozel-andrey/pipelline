package com.moneyman.pipeline.services;

import com.moneyman.pipeline.data.dao.PipelineExecutionRepository;
import com.moneyman.pipeline.data.dao.PipelineRepository;
import com.moneyman.pipeline.data.dto.PipelineExecutionDto;
import com.moneyman.pipeline.data.entity.*;
import com.moneyman.pipeline.data.mapper.PipelineExecutionMapper;
import com.moneyman.pipeline.exceptions.ExecutionFailedException;
import com.moneyman.pipeline.services.actions.ActionExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class ExecutionsService {

    private Logger LOG = LoggerFactory.getLogger(ExecutionsService.class);

    @Autowired
    private PipelineRepository pipelineRepository;
    @Autowired
    private PipelineExecutionRepository executionRepository;

    @Autowired
    private PipelineExecutionMapper executionMapper;

    @Autowired
    private ActionExecutor actionExecutor;

    // What is the context name in task description?
    @Transactional(Transactional.TxType.REQUIRED)
    public Long initExecution(String pipelineName, String contextName) {
        Pipeline pipeline = pipelineRepository.findByName(pipelineName);
        PipelineExecution execution = new PipelineExecution();
        execution.setStatus(ExecutionStatus.PENDING);
        execution.setPipeline(pipeline.getName());
        pipeline.getTasks().forEach(t -> createPipelineExecutions(t, execution));
        LOG.info("Create execution for pipeline: " + pipelineName);
        executionRepository.save(execution);
        return execution.getId();
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void execute(Long id) {
        PipelineExecution execution = executionRepository.getOne(id);
        Pipeline pipeline = pipelineRepository.findByName(execution.getPipeline());
        execution.setStatus(ExecutionStatus.IN_PROGRESS);
        execution.setStartTime(new Date());
        LOG.info("Execution for pipeline: " + execution.getPipeline() + " is started");
        Map<String, Future<ExecutionStatus>> statuses = new HashMap<>();
        for (PipelineTaskExecution task : execution.getTasks()) {
            if(executionRepository.isCancelled(execution.getId())) {
                shutdownExecution(execution);
                return;
            }

            try {
                checkParentsCompleted(pipeline, task.getName(), statuses);
                statuses.put(task.getName(), actionExecutor.execute(task));
            } catch(ExecutionFailedException ex) {
                failExecution(task, execution);
                return;
            }
        }
        checkIsCompleted(statuses);
        LOG.info("Execution for pipeline: " + execution.getPipeline() + " is completed");
        execution.setStatus(ExecutionStatus.COMPLETED);
        execution.setEndTime(new Date());
        executionRepository.save(execution);
    }

    private void checkIsCompleted(Map<String, Future<ExecutionStatus>> statuses) {
        statuses.forEach((action, future) -> {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

    private void checkParentsCompleted(Pipeline pipeline, String task, Map<String, Future<ExecutionStatus>> statuses) {
        try {
            List<String> parents = pipeline.getParents(task);
            for (String parent : parents) {
                Future<ExecutionStatus> future = statuses.get(parent);
                ExecutionStatus status = future.get();
                if(status == ExecutionStatus.FAILED) {
                    throw new ExecutionFailedException(parent);
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void markToShutdown(Long id) {
        PipelineExecution execution = executionRepository.getOne(id);
        execution.setCanceled(true);
        executionRepository.save(execution);
    }

    public PipelineExecutionDto getState(Long id) {
        PipelineExecution execution = executionRepository.getOne(id);
        return executionMapper.toDto(execution);
    }

    private void createPipelineExecutions(PipelineTask task, PipelineExecution execution) {
        PipelineTaskExecution taskExecution = new PipelineTaskExecution();
        taskExecution.setName(task.getName());
        taskExecution.setStatus(ExecutionStatus.PENDING);
        taskExecution.setExecution(execution);
        taskExecution.setAction(task.getAction());
        execution.addTask(taskExecution);
    }

    private void failExecution(PipelineTaskExecution task, PipelineExecution execution) {
        LOG.info("Execution for pipeline: " + execution.getPipeline() + " is failed");
        execution.setEndTime(new Date());
        execution.setStatus(ExecutionStatus.FAILED);
        executionRepository.save(execution);
    }

    private void shutdownExecution(PipelineExecution execution) {
        LOG.info("Execution for pipeline: " + execution.getPipeline() + " is cancelled");
        execution.setStatus(ExecutionStatus.SKIPPED);
        execution.setEndTime(new Date());
        executionRepository.save(execution);
    }

}
