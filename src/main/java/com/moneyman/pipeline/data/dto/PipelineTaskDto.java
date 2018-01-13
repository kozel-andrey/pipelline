package com.moneyman.pipeline.data.dto;

public class PipelineTaskDto {

    private String name;
    private String description;
    private PipelineTaskActionDto action;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PipelineTaskActionDto getAction() {
        return action;
    }

    public void setAction(PipelineTaskActionDto action) {
        this.action = action;
    }
}
