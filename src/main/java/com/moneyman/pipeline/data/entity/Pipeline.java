package com.moneyman.pipeline.data.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "pipeline")
public class Pipeline extends BaseEntity {

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @OrderBy("id ASC")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "pipeline")
    private List<PipelineTask> tasks = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "pipeline")
    private List<PipelineTransition> transitions = new ArrayList<>();

    public List<PipelineTransition> getTransitions() {
        return transitions;
    }

    public void setTransitions(List<PipelineTransition> transitions) {
        this.transitions = transitions;
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

    public List<PipelineTask> getTasks() {
        return tasks;
    }

    public void setTasks(List<PipelineTask> tasks) {
        this.tasks = tasks;
    }

    public PipelineTask getFirstTask() {
        if(tasks.isEmpty()) return null;

        return tasks.get(0);
    }

    public List<PipelineTask> findNextTasks(PipelineTask task) {
        List<String> nextTasks = transitions.stream().filter(tr -> tr.getTask().equals(task.getName()))
                .map(PipelineTransition::getNextTask).collect(Collectors.toList());

        return tasks.stream().filter(t -> nextTasks.contains(t.getName())).collect(Collectors.toList());
    }
}
