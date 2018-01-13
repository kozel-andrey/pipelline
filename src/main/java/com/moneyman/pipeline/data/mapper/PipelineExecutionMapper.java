package com.moneyman.pipeline.data.mapper;

import com.moneyman.pipeline.data.dto.PipelineExecutionDto;
import com.moneyman.pipeline.data.dto.PipelineTaskExecutionDto;
import com.moneyman.pipeline.data.entity.PipelineExecution;
import com.moneyman.pipeline.data.entity.PipelineTaskExecution;
import org.springframework.stereotype.Component;

@Component
public class PipelineExecutionMapper {

    public PipelineExecutionDto toDto(PipelineExecution execution) {
        PipelineExecutionDto dto = new PipelineExecutionDto();
        dto.setExecutionId(execution.getId());
        dto.setPipeline(execution.getPipeline());
        dto.setStartTime(execution.getStartTime());
        dto.setStatus(execution.getStatus());
        execution.getTasks().forEach(t -> dto.getTasks().add(toDto(t)));
        return dto;
    }

    private PipelineTaskExecutionDto toDto(PipelineTaskExecution task) {
        PipelineTaskExecutionDto dto = new PipelineTaskExecutionDto();
        dto.setName(task.getName());
        dto.setStatus(task.getStatus());
        dto.setStartTime(task.getStartTime());
        dto.setEndTime(task.getEndTime());
        return dto;
    }

}
