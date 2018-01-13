package com.moneyman.pipeline.data.dao;

import com.moneyman.pipeline.data.entity.PipelineExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PipelineExecutionRepository extends JpaRepository<PipelineExecution, Long> {

}
