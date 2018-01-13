package com.moneyman.pipeline.data.dao;

import com.moneyman.pipeline.data.entity.PipelineTaskExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PipelineExecutionTaskRepository extends JpaRepository<PipelineTaskExecution, Long> {


}
