package com.moneyman.pipeline.data.mapper;

import com.moneyman.pipeline.data.dto.PipelineDto;
import com.moneyman.pipeline.data.dto.PipelineTaskActionDto;
import com.moneyman.pipeline.data.dto.PipelineTaskDto;
import com.moneyman.pipeline.data.entity.Pipeline;
import com.moneyman.pipeline.data.entity.PipelineTask;
import org.springframework.stereotype.Component;

@Component
public class PipelineMapper {

    public Pipeline fromDto(PipelineDto dto) {
        Pipeline pipeline = new Pipeline();
        pipeline.setName(dto.getName());
        pipeline.setDescription(dto.getDescription());
        dto.getTasks().forEach(t -> pipeline.getTasks().add(fromDto(t, pipeline)));
        return pipeline;
    }

    public PipelineDto toDto(Pipeline pipeline) {
        PipelineDto dto = new PipelineDto();
        dto.setId(pipeline.getId());
        dto.setName(pipeline.getName());
        dto.setDescription(pipeline.getDescription());
        pipeline.getTasks().forEach(t -> dto.getTasks().add(toDto(t)));
        return dto;
    }

    public void update(Pipeline pipeline, PipelineDto dto) {
        pipeline.setName(dto.getName());
        pipeline.setDescription(dto.getDescription());
        pipeline.getTasks().clear();
        dto.getTasks().forEach(t -> pipeline.getTasks().add(fromDto(t, pipeline)));
    }


    private PipelineTaskDto toDto(PipelineTask task) {
        PipelineTaskDto dto = new PipelineTaskDto();
        dto.setName(task.getName());
        dto.setDescription(task.getDescription());
        dto.setAction(new PipelineTaskActionDto(task.getAction()));
        return dto;
    }

    private PipelineTask fromDto(PipelineTaskDto dto, Pipeline pipeline) {
        PipelineTask task = new PipelineTask();
        task.setName(dto.getName());
        task.setDescription(dto.getDescription());
        task.setAction(dto.getAction().getType());
        task.setPipeline(pipeline);
        return task;
    }

}
