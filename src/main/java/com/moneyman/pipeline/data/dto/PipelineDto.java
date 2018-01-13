package com.moneyman.pipeline.data.dto;

import org.springframework.data.util.Pair;

import java.util.List;

public class PipelineDto {

    private String name;
    private String description;
    private List<PipelineTaskDto> tasks;
    private List<Pair<String, String>> transitions;

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

    public List<PipelineTaskDto> getTasks() {
        return tasks;
    }

    public void setTasks(List<PipelineTaskDto> tasks) {
        this.tasks = tasks;
    }

    public List<Pair<String, String>> getTransitions() {
        return transitions;
    }

    public void setTransitions(List<Pair<String, String>> transitions) {
        this.transitions = transitions;
    }
}
