package com.moneyman.pipeline.services.actions;

import com.moneyman.pipeline.data.entity.ExecutionStatus;
import org.springframework.stereotype.Component;

@Component
public class RandomAction extends AbstractPipelineAction {

    @Override
    public String getType() {
        return "RANDOM";
    }

    @Override
    protected int getSleepTime() {
        return 1000;
    }

    @Override
    protected ExecutionStatus getStatus() {
        int index = (int) (Math.random() * 5);

        return ExecutionStatus.values()[index];
    }
}
