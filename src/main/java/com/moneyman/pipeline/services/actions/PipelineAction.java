package com.moneyman.pipeline.services.actions;

import com.moneyman.pipeline.data.entity.PipelineTaskExecution;

public interface PipelineAction {

    void execute(PipelineTaskExecution task);

    String getType();

}
