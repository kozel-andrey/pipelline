package com.moneyman.pipeline.services;

import com.moneyman.pipeline.data.dto.PipelineDto;
import com.moneyman.pipeline.data.entity.Pipeline;
import org.springframework.stereotype.Service;

@Service
public class PipelineService {

    public Long createPipeline(PipelineDto dto) {
        return -1L;
    }

    public Pipeline getPipeline(Long id) {
        return null;
    }

    public void updatePipeline(Long id, PipelineDto pipeline) {

    }

    public void deletePipeline(Long id) {

    }
}
