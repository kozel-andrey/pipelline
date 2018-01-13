package com.moneyman.pipeline.services.actions;

import com.moneyman.pipeline.data.dao.PipelineExecutionTaskRepository;
import com.moneyman.pipeline.data.entity.PipelineTaskExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ActionExecutor {

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


    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void execute(PipelineTaskExecution task) {
        PipelineAction executor = getExecutor(task.getAction());
        executor.execute(task);
        taskRepository.save(task);
    }

    private PipelineAction getExecutor(String action) {
        return executors.get(action.toUpperCase());
    }

}
