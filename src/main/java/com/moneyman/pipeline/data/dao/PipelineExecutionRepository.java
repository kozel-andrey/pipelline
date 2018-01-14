package com.moneyman.pipeline.data.dao;

import com.moneyman.pipeline.data.entity.PipelineExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PipelineExecutionRepository extends JpaRepository<PipelineExecution, Long> {

    @Query("SELECT p.canceled FROM PipelineExecution p WHERE p.id = :id")
    boolean isCancelled(@Param("id") Long id);

}
