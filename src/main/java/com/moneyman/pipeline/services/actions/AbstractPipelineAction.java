package com.moneyman.pipeline.services.actions;

import com.moneyman.pipeline.data.entity.ExecutionStatus;
import com.moneyman.pipeline.data.entity.PipelineTaskExecution;

import java.util.Date;

public abstract class AbstractPipelineAction implements PipelineAction {

    @Override
    public void execute(PipelineTaskExecution task) {
        try {
            task.setStartTime(new Date());
            Thread.sleep(getSleepTime());
            task.setStatus(getStatus());
        } catch (Exception ex) {
            task.setStatus(ExecutionStatus.FAILED);
        } finally {
            task.setEndTime(new Date());
        }
    }

    protected abstract ExecutionStatus getStatus();

    protected abstract int getSleepTime();

}
