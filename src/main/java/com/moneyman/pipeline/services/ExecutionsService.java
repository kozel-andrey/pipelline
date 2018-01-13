package com.moneyman.pipeline.services;

import com.moneyman.pipeline.data.dao.PipelineExecutionRepository;
import com.moneyman.pipeline.data.dao.PipelineRepository;
import com.moneyman.pipeline.data.dto.PipelineExecutionDto;
import com.moneyman.pipeline.data.entity.*;
import com.moneyman.pipeline.data.mapper.PipelineExecutionMapper;
import com.moneyman.pipeline.services.actions.ActionExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ExecutionsService {

    @Autowired
    private PipelineRepository pipelineRepository;
    @Autowired
    private PipelineExecutionRepository executionRepository;

    @Autowired
    private PipelineExecutionMapper executionMapper;

    @Autowired
    private ActionExecutor actionExecutor;

    // What is the context name?
    public Long initExecution(String pipelineName, String contextName) {
        Pipeline pipeline = pipelineRepository.findByName(pipelineName);
        PipelineExecution execution = new PipelineExecution();
        execution.setStatus(ExecutionStatus.PENDING);
        execution.setPipeline(pipeline.getName());

        if(!pipeline.getTasks().isEmpty()) {
            createPipelineExecutions(pipeline.getFirstTask(), pipeline, execution);
        }

        executionRepository.save(execution);
        return execution.getId();
    }

    @Async
    public void execute(Long id) {
        PipelineExecution execution = executionRepository.getOne(id);
        execution.setStatus(ExecutionStatus.IN_PROGRESS);
        execution.setStartTime(new Date());
        execution.getTasks().forEach(actionExecutor::execute);
    }

    public PipelineExecutionDto getState(Long id) {
        PipelineExecution execution = executionRepository.getOne(id);
        return executionMapper.toDto(execution);
    }

    private void createPipelineExecutions(PipelineTask task, Pipeline pipeline, PipelineExecution execution) {
        PipelineTaskExecution taskExecution = new PipelineTaskExecution();
        taskExecution.setName(task.getName());
        taskExecution.setStatus(ExecutionStatus.PENDING);
        taskExecution.setExecution(execution);
        taskExecution.setAction(task.getAction());
        List<PipelineTask> nextTasks = pipeline.findNextTasks(task);
        nextTasks.forEach(t -> createPipelineExecutions(t, pipeline, execution));
    }

    public void shutdown(Long id) {
        PipelineExecution execution = executionRepository.getOne(id);
        execution.setCanceled(true);
        executionRepository.save(execution);
    }

}
