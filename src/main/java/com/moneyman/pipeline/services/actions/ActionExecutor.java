package com.moneyman.pipeline.services.actions;

import com.moneyman.pipeline.data.dao.PipelineExecutionTaskRepository;
import com.moneyman.pipeline.data.entity.ExecutionStatus;
import com.moneyman.pipeline.data.entity.PipelineTaskExecution;
import com.moneyman.pipeline.exceptions.ExecutionFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

@Component
public class ActionExecutor {

    private Logger LOG = LoggerFactory.getLogger(ActionExecutor.class);

    @Autowired
    private PipelineExecutionTaskRepository taskRepository;

    @Autowired
    private List<PipelineAction> actions;

    private Map<String, PipelineAction> executors;

    @PostConstruct
    public void fillExecutors() {
        executors = new HashMap<>();
        actions.forEach(a -> executors.put(a.getType(), a));
    }


    @Transactional(value = Transactional.TxType.REQUIRED, dontRollbackOn = ExecutionFailedException.class)
    public Future<ExecutionStatus> execute(PipelineTaskExecution task) {
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

    private PipelineAction getExecutor(String action) {
        return executors.get(action.toUpperCase());
    }

}
