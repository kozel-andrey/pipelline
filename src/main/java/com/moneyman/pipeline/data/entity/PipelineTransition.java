package com.moneyman.pipeline.data.entity;

import javax.persistence.*;

@Entity
@Table(name = "pipeline_transition")
public class PipelineTransition extends BaseEntity {

    @Column(name = "task")
    private String task;

    @Column(name= "next_task_name")
    private String nextTask;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pipeline_id")
    private Pipeline pipeline;

    public PipelineTransition() {
    }

    public PipelineTransition(String task, String nextTask, Pipeline pipeline) {
        this.task = task;
        this.nextTask = nextTask;
        this.pipeline = pipeline;
    }

    public Pipeline getPipeline() {
        return pipeline;
    }

    public void setPipeline(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getNextTask() {
        return nextTask;
    }

    public void setNextTask(String nextTask) {
        this.nextTask = nextTask;
    }
}
