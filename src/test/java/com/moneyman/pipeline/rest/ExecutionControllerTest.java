package com.moneyman.pipeline.rest;

import com.moneyman.pipeline.PipelineApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
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
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    private final String APPLICATION_YAML_UTF_8 = "application/x-yaml;charset=UTF-8";

    @Before
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void test_startExecution_success() throws Exception {
        mockMvc.perform(post("/executions/execute")
                .contentType("application/x-yaml")
                .content(getExecuteRequest())
                .accept(MediaType.parseMediaType(APPLICATION_YAML_UTF_8)))
                .andExpect(status().isOk());
    }

    @Test
    public void test_startExecutionState_success() throws Exception {
        mockMvc.perform(get("/executions/1/state")
                .contentType("application/x-yaml")
                .accept(MediaType.parseMediaType(APPLICATION_YAML_UTF_8)))
                .andExpect(status().isOk());
    }

    @Test
    public void test_shutdownExecution_success() throws Exception {
        mockMvc.perform(post("/executions/1/shutdown")
                .contentType("application/x-yaml")
                .accept(MediaType.parseMediaType(APPLICATION_YAML_UTF_8)))
                .andExpect(status().isOk());
    }

    public String getExecuteRequest() {
        StringBuilder request = new StringBuilder();
        request.append("pipelineName: another name\n");
        request.append("contextName: another description\n");
        return request.toString();
    }
}
