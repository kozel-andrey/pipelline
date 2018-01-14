package com.moneyman.pipeline.services.actions;

import com.moneyman.pipeline.data.dao.PipelineExecutionRepository;
import com.moneyman.pipeline.data.dao.PipelineExecutionTaskRepository;
import com.moneyman.pipeline.data.entity.ExecutionStatus;
import com.moneyman.pipeline.data.entity.Pipeline;
import com.moneyman.pipeline.data.entity.PipelineExecution;
import com.moneyman.pipeline.data.entity.PipelineTaskExecution;
import com.moneyman.pipeline.exceptions.ExecutionFailedException;
import com.moneyman.pipeline.rest.TaskExecutionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Component
public class ActionExecutor {

    private Logger LOG = LoggerFactory.getLogger(ActionExecutor.class);

    @Autowired
    private PipelineExecutionTaskRepository taskRepository;
    @Autowired
    private PipelineExecutionRepository executionRepository;
    @Autowired
    private TaskExecutionService taskExecutionService;

    @Autowired
    private List<PipelineAction> actions;

    private Map<String, PipelineAction> executors;

    @PostConstruct
    public void fillExecutors() {
        executors = new HashMap<>();
        actions.forEach(a -> executors.put(a.getType(), a));
    }


    @Async
    @Transactional(value = Transactional.TxType.REQUIRED, dontRollbackOn = ExecutionFailedException.class)
    public Future<ExecutionStatus> execute(PipelineTaskExecution task, PipelineExecution execution,
                                           Pipeline pipeline, Map<String, Future<ExecutionStatus>> statuses) {
        if(executionRepository.isCancelled(execution.getId())) {
            shutdownExecution(execution);
            return new AsyncResult<>(task.getStatus());
        }

        try {
            checkParentsCompleted(pipeline, task.getName(), statuses);
            return execute(task);
        } catch(ExecutionFailedException ex) {
            failExecution(task, execution);
            return new AsyncResult<>(task.getStatus());
        }

    }

    private Future<ExecutionStatus> execute(PipelineTaskExecution task) {
        taskExecutionService.setInProgress(task.getId());
        task = taskRepository.findOne(task.getId());
        LOG.info("Started execution of task with name: " + task.getName());
        PipelineAction executor = getExecutor(task.getAction());
        executor.execute(task);
        taskRepository.save(task);
        if(task.getStatus() == ExecutionStatus.FAILED) {
            LOG.info("Failed execution of task with name: " + task.getName());
            throw new ExecutionFailedException(task.getName());
        }
        LOG.info("Ended execution of task with name: " + task.getName());
        return new AsyncResult<>(task.getStatus());
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

    private PipelineAction getExecutor(String action) {
        return executors.get(action.toUpperCase());
    }

}
