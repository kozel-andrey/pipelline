package com.moneyman.pipeline.rest;

import com.moneyman.pipeline.PipelineApplication;
import com.moneyman.pipeline.data.dao.PipelineExecutionRepository;
import com.moneyman.pipeline.data.dao.PipelineRepository;
import com.moneyman.pipeline.data.entity.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = PipelineApplication.class)
@WebAppConfiguration
@Transactional
public class ExecutionControllerTest {

    @Autowired
    private PipelineExecutionRepository executionRepository;
    @Autowired
    private PipelineRepository pipelineRepository;
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    private final String APPLICATION_YAML_UTF_8 = "application/x-yaml;charset=UTF-8";

    @Before
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void test_startExecution_success() throws Exception {
        getAndSavePipeline();
        mockMvc.perform(post("/executions/execute")
                .contentType("application/x-yaml")
                .content(getExecuteRequest())
                .accept(MediaType.parseMediaType(APPLICATION_YAML_UTF_8)))
                .andExpect(status().isOk());

        Assert.assertEquals(executionRepository.count(), 1);
    }

    @Test
    public void test_getExecutionState_success() throws Exception {
        PipelineExecution execution = getAndSaveExecution();
        MvcResult mvcResult = mockMvc.perform(get("/executions/" + execution.getId() + "/state")
                .contentType("application/x-yaml")
                .accept(MediaType.parseMediaType(APPLICATION_YAML_UTF_8)))
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();

        Assert.assertTrue(content.contains("some pipeline"));
        Assert.assertTrue(content.contains("PENDING"));
        Assert.assertTrue(content.contains(execution.getId().toString()));
    }

    @Test
    public void test_shutdownExecution_success() throws Exception {
        PipelineExecution execution = getAndSaveExecution();
        Assert.assertFalse(execution.isCanceled());
        mockMvc.perform(post("/executions/" + execution.getId() + "/shutdown")
                .contentType("application/x-yaml")
                .accept(MediaType.parseMediaType(APPLICATION_YAML_UTF_8)))
                .andExpect(status().isOk());

        PipelineExecution changedExecution = executionRepository.getOne(execution.getId());
        Assert.assertTrue(changedExecution.isCanceled());
    }

    public String getExecuteRequest() {
        StringBuilder request = new StringBuilder();
        request.append("pipelineName: some name\n");
        request.append("contextName: some description\n");
        return request.toString();
    }

    public Pipeline getAndSavePipeline() {
        Pipeline pipeline = new Pipeline();
        pipeline.setName("some name");
        pipeline.setDescription("some description");
        pipeline.getTasks().add(new PipelineTask("Build", "Build", "Completed", pipeline));
        pipeline.getTasks().add(new PipelineTask("Test", "Test", "Print", pipeline));
        pipeline.getTasks().add(new PipelineTask("Docs", "Docs", "Delayed", pipeline));
        pipeline.getTasks().add(new PipelineTask("Integration tests", "Integration tests", "Completed", pipeline));
        pipeline.getTasks().add(new PipelineTask("Publish", "Publish", "Delayed", pipeline));
        pipeline.getTasks().add(new PipelineTask("Sync", "Sync", "Completed", pipeline));
        pipeline.getTransitions().add(new PipelineTransition("Build", "Test", pipeline));
        pipeline.getTransitions().add(new PipelineTransition("Test", "Docs", pipeline));
        pipeline.getTransitions().add(new PipelineTransition("Test", "Integration tests", pipeline));
        pipeline.getTransitions().add(new PipelineTransition("Integration tests", "Publish", pipeline));
        pipeline.getTransitions().add(new PipelineTransition("Publish", "Sync", pipeline));
        pipeline.getTransitions().add(new PipelineTransition("Docs", "Sync", pipeline));

        pipelineRepository.save(pipeline);
        return pipeline;
    }

    public PipelineExecution getAndSaveExecution() {
        PipelineExecution execution = new PipelineExecution();
        execution.setPipeline("some pipeline");
        execution.setStatus(ExecutionStatus.PENDING);
        execution.setCanceled(false);
        executionRepository.save(execution);
        return execution;
    }
}
