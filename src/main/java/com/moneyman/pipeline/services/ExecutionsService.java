package com.moneyman.pipeline.services;

import com.moneyman.pipeline.data.dao.PipelineExecutionRepository;
import com.moneyman.pipeline.data.dao.PipelineRepository;
import com.moneyman.pipeline.data.dto.PipelineExecutionDto;
import com.moneyman.pipeline.data.entity.*;
import com.moneyman.pipeline.data.mapper.PipelineExecutionMapper;
import com.moneyman.pipeline.services.actions.ActionExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
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
    @Transactional(Transactional.TxType.REQUIRES_NEW)
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


    public PipelineExecutionDto execute(Long id) {
        PipelineExecution execution = executionRepository.getOne(id);
        Pipeline pipeline = pipelineRepository.findByName(execution.getPipeline());
        execution.setStatus(ExecutionStatus.IN_PROGRESS);
        execution.setStartTime(new Date());
        LOG.info("Execution for pipeline: " + execution.getPipeline() + " is started");
        Map<String, Future<ExecutionStatus>> statuses = new HashMap<>();
        for (PipelineTaskExecution task : execution.getTasks()) {
            Future<ExecutionStatus> executionResult = actionExecutor.execute(task, execution, pipeline, statuses);
            statuses.put(task.getName(), executionResult);
        }
        checkIsCompleted(statuses);
        LOG.info("Execution for pipeline: " + execution.getPipeline() + " is completed");
        execution.setStatus(ExecutionStatus.COMPLETED);
        execution.setEndTime(new Date());
        executionRepository.save(execution);
        return executionMapper.toDto(execution);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public PipelineExecutionDto markToShutdown(Long id) {
        PipelineExecution execution = executionRepository.getOne(id);
        execution.setCanceled(true);
        executionRepository.save(execution);
        return executionMapper.toDto(execution);
    }

    public PipelineExecutionDto getState(Long id) {
        PipelineExecution execution = executionRepository.getOne(id);
        return executionMapper.toDto(execution);
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

    private void createPipelineExecutions(PipelineTask task, PipelineExecution execution) {
        PipelineTaskExecution taskExecution = new PipelineTaskExecution();
        taskExecution.setName(task.getName());
        taskExecution.setStatus(ExecutionStatus.CANCELED);
        taskExecution.setExecution(execution);
        taskExecution.setAction(task.getAction());
        execution.addTask(taskExecution);
    }

}
