package com.moneyman.pipeline.data.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "pipeline_execution")
public class PipelineExecution extends BaseEntity {

    @NotNull
    @Column(name = "pipeline")
    private String pipeline;

    @Column(name = "status")
    private ExecutionStatus status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_time")
    private Date startTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_time")
    private Date endTime;

    @Column(name = "is_canceled")
    private boolean canceled = false;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "execution")
    private List<PipelineTaskExecution> tasks = new ArrayList<>();

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public List<PipelineTaskExecution> getTasks() {
        return tasks;
    }

    public void setTasks(List<PipelineTaskExecution> tasks) {
        this.tasks = tasks;
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


    public void addTask(PipelineTaskExecution task) {
        tasks.add(task);
    }
}
