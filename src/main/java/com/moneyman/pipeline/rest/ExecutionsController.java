package com.moneyman.pipeline.rest;

import com.moneyman.pipeline.data.dto.PipelineExecutionDto;
import com.moneyman.pipeline.services.ExecutionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/executions", consumes = "application/x-yaml", produces = "application/x-yaml")
public class ExecutionsController {

    @Autowired
    private ExecutionsService executionsService;

    @RequestMapping(value = "execute", method = RequestMethod.POST)
    public ResponseEntity execute(@RequestBody ExecutionRequest request) {
        Long id = executionsService.initExecution(request.getPipelineName(), request.getContextName());
        executionsService.execute(id);
        return ResponseEntity.ok(id);
    }

    @RequestMapping(value = "{id}/state", method = RequestMethod.GET)
    public ResponseEntity showState(@PathVariable Long id) {
        PipelineExecutionDto execution = executionsService.getState(id);
        return ResponseEntity.ok(execution);
    }

    @RequestMapping(value = "{id}/shutdown")
    public ResponseEntity shutdown(@PathVariable Long id) {
        executionsService.shutdown(id);
        return ResponseEntity.ok().build();
    }

    public static class ExecutionRequest {

        private String pipelineName;
        private String contextName;

        public String getPipelineName() {
            return pipelineName;
        }

        public void setPipelineName(String pipelineName) {
            this.pipelineName = pipelineName;
        }

        public String getContextName() {
            return contextName;
        }

        public void setContextName(String contextName) {
            this.contextName = contextName;
        }
    }

}
