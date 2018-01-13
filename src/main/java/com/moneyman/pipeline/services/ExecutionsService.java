package com.moneyman.pipeline.services;

import com.moneyman.pipeline.data.dto.PipelineExecutionDto;
import org.springframework.stereotype.Service;

@Service
public class ExecutionsService {

    public Long execute(String pipelineName, String contextName) {
        return -1L;
    }

    public PipelineExecutionDto getState(Long id) {
        return new PipelineExecutionDto();
    }

    public void shutdown(Long id) {

    }
}
