package com.moneyman.pipeline.rest;

import com.moneyman.pipeline.data.dao.PipelineExecutionTaskRepository;
import com.moneyman.pipeline.data.entity.ExecutionStatus;
import com.moneyman.pipeline.data.entity.PipelineTaskExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class TaskExecutionService {

    @Autowired
    private PipelineExecutionTaskRepository taskRepository;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void setInProgress(Long taskId) {
        PipelineTaskExecution task = taskRepository.getOne(taskId);
        task.setStatus(ExecutionStatus.IN_PROGRESS);
        taskRepository.save(task);
    }

}
