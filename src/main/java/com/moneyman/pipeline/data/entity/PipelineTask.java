package com.moneyman.pipeline.data.entity;

import javax.persistence.*;

@Entity
@Table(name = "pipeline_task")
public class PipelineTask extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "action")
    private String action;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pipeline_id")
    private Pipeline pipeline;

    public PipelineTask(String name, String description, String action, Pipeline pipeline) {
        this.name = name;
        this.description = description;
        this.action = action;
        this.pipeline = pipeline;
    }

    public PipelineTask() {
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Pipeline getPipeline() {
        return pipeline;
    }

    public void setPipeline(Pipeline pipeline) {
        this.pipeline = pipeline;
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

}
