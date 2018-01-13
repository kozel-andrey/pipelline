package com.moneyman.pipeline.data.dto;

public class PipelineTaskActionDto {

    private String type;

    public PipelineTaskActionDto() {
    }

    public PipelineTaskActionDto(String action) {
        type = action;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
