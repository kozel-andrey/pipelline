package com.moneyman.pipeline.services;

import com.moneyman.pipeline.data.dao.PipelineRepository;
import com.moneyman.pipeline.data.dto.PipelineDto;
import com.moneyman.pipeline.data.entity.Pipeline;
import com.moneyman.pipeline.data.mapper.PipelineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PipelineService {

    @Autowired
    private PipelineRepository pipelineRepository;
    @Autowired
    private PipelineMapper pipelineMapper;

    public Long createPipeline(PipelineDto dto) {
        Pipeline pipeline = pipelineMapper.fromDto(dto);
        pipelineRepository.save(pipeline);
        return pipeline.getId();
    }

    public PipelineDto getPipeline(Long id) {
        Pipeline pipeline = pipelineRepository.getOne(id);
        PipelineDto dto = pipelineMapper.toDto(pipeline);
        return dto;
    }

    public void updatePipeline(Long id, PipelineDto dto) {
        Pipeline pipeline = pipelineRepository.getOne(id);
        pipelineMapper.update(pipeline, dto);
        pipelineRepository.save(pipeline);
    }

    public void deletePipeline(Long id) {
        pipelineRepository.delete(id);
    }
}
