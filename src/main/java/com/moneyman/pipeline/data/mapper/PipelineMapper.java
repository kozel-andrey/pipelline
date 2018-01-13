package com.moneyman.pipeline.data.mapper;

import com.moneyman.pipeline.data.dto.PipelineDto;
import com.moneyman.pipeline.data.dto.PipelineTaskActionDto;
import com.moneyman.pipeline.data.dto.PipelineTaskDto;
import com.moneyman.pipeline.data.entity.Pipeline;
import com.moneyman.pipeline.data.entity.PipelineTask;
import com.moneyman.pipeline.data.entity.PipelineTransition;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PipelineMapper {

    public Pipeline fromDto(PipelineDto dto) {
        Pipeline pipeline = new Pipeline();
        pipeline.setName(dto.getName());
        pipeline.setDescription(dto.getDescription());
        dto.getTasks().forEach(t -> pipeline.getTasks().add(fromDto(t, pipeline)));
        dto.getTransitions().forEach(t -> pipeline.getTransitions().addAll(fromDto(t, pipeline)));
        return pipeline;
    }

    public PipelineDto toDto(Pipeline pipeline) {
        PipelineDto dto = new PipelineDto();
        dto.setId(pipeline.getId());
        dto.setName(pipeline.getName());
        dto.setDescription(pipeline.getDescription());
        pipeline.getTasks().forEach(t -> dto.getTasks().add(toDto(t)));
        pipeline.getTransitions().forEach(t -> dto.getTransitions().add(toDto(t)));
        return dto;
    }

    private Map<String, String> toDto(PipelineTransition transition) {
        Map<String, String> dto = new HashMap<>();
        dto.put(transition.getTask(), transition.getNextTask());
        return dto;
    }

    public void update(Pipeline pipeline, PipelineDto dto) {
        pipeline.setName(dto.getName());
        pipeline.setDescription(dto.getDescription());
        pipeline.getTasks().clear();
        dto.getTasks().forEach(t -> pipeline.getTasks().add(fromDto(t, pipeline)));
    }

    private PipelineTask fromDto(PipelineTaskDto dto, Pipeline pipeline) {
        PipelineTask task = new PipelineTask();
        task.setName(dto.getName());
        task.setDescription(dto.getDescription());
        task.setAction(dto.getAction().getType());
        task.setPipeline(pipeline);
        return task;
    }

    private PipelineTaskDto toDto(PipelineTask task) {
        PipelineTaskDto dto = new PipelineTaskDto();
        dto.setName(task.getName());
        dto.setDescription(task.getDescription());
        dto.setAction(new PipelineTaskActionDto(task.getAction()));
        return dto;
    }

    private List<PipelineTransition> fromDto(Map<String, String> pair, Pipeline pipeline) {
        List<PipelineTransition> transition = new ArrayList<>();
        pair.forEach((task, nextTask) -> transition.add(new PipelineTransition(task, nextTask, pipeline)));
        return transition;
    }

}
