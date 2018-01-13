package com.moneyman.pipeline.data.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PipelineDto {

    private Long id;
    private String name;
    private String description;
    private List<PipelineTaskDto> tasks = new ArrayList<>();
    private List<Map<String, String>> transitions = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public List<Map<String, String>> getTransitions() {
        return transitions;
    }

    public void setTransitions(List<Map<String, String>> transitions) {
        this.transitions = transitions;
    }
}
