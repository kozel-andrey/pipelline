package com.moneyman.pipeline.services.actions;

import com.moneyman.pipeline.data.entity.ExecutionStatus;
import org.springframework.stereotype.Component;

@Component
public class PrintAction extends AbstractPipelineAction {

    @Override
    protected ExecutionStatus getStatus() {
        return ExecutionStatus.COMPLETED;
    }

    @Override
    protected int getSleepTime() {
        return 0;
    }

    @Override
    public String getType() {
        return "PRINT";
    }
}
