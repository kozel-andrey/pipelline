package com.moneyman.pipeline.rest;

import com.moneyman.pipeline.data.dto.PipelineDto;
import com.moneyman.pipeline.data.entity.Pipeline;
import com.moneyman.pipeline.services.PipelineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/pipeline", consumes = "application/x-yaml", produces = "application/x-yaml")
public class PipelineController {

    @Autowired
    private PipelineService pipelineService;

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity create(@RequestBody PipelineDto pipeline) {
        Long id = pipelineService.createPipeline(pipeline);
        return ResponseEntity.ok(id);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.POST)
    public ResponseEntity update(@PathVariable Long id, @RequestBody PipelineDto pipeline) {
        pipelineService.updatePipeline(id, pipeline);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity get(@PathVariable Long id) {
        PipelineDto pipeline = pipelineService.getPipeline(id);
        return ResponseEntity.ok(pipeline);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable Long id) {
        pipelineService.deletePipeline(id);
        return ResponseEntity.ok().build();
    }

}
