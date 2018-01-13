package com.moneyman.pipeline.data.dto;

import com.moneyman.pipeline.data.entity.ExecutionStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PipelineExecutionDto {

    private Long executionId;
    private String pipeline;
    private ExecutionStatus status;
    private Date startTime;
    private List<PipelineTaskExecutionDto> tasks = new ArrayList<>();

    public List<PipelineTaskExecutionDto> getTasks() {
        return tasks;
    }

    public void setTasks(List<PipelineTaskExecutionDto> tasks) {
        this.tasks = tasks;
    }

    public Long getExecutionId() {
        return executionId;
    }

    public void setExecutionId(Long executionId) {
        this.executionId = executionId;
    }

    public String getPipeline() {
        return pipeline;
    }

    public void setPipeline(String pipeline) {
        this.pipeline = pipeline;
    }

    public ExecutionStatus getStatus() {
        return status;
    }

    public void setStatus(ExecutionStatus status) {
        this.status = status;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
}
