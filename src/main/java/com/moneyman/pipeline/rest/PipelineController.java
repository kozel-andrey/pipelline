package com.moneyman.pipeline.rest;

import com.moneyman.pipeline.data.dto.PipelineDto;
import com.moneyman.pipeline.services.PipelineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/pipeline", consumes = "application/x-yaml")
public class PipelineController {

    @Autowired
    private PipelineService pipelineService;


    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity create(@RequestBody PipelineDto pipeline) {
        pipelineService.createPipeline(pipeline);
        return ResponseEntity.ok().build();
    }

}
