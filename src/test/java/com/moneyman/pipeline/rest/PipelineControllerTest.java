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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = PipelineApplication.class)
@WebAppConfiguration
@Transactional
public class PipelineControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    private final String APPLICATION_YAML_UTF_8 = "application/x-yaml;charset=UTF-8";

    @Before
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void test_create_successRequest() throws Exception {
        mockMvc.perform(put("/pipeline")
                .contentType("application/x-yaml")
                .content(getYamlPipeline())
                .accept(MediaType.parseMediaType(APPLICATION_YAML_UTF_8)))
                .andExpect(status().isOk());
    }

    @Test
    public void test_update_successRequest() throws Exception {
        mockMvc.perform(post("/pipeline/1")
                .contentType("application/x-yaml")
                .content(getYamlPipeline())
                .accept(MediaType.parseMediaType(APPLICATION_YAML_UTF_8)))
                .andExpect(status().isOk());
    }

    @Test
    public void test_delete_successRequest() throws Exception {
        mockMvc.perform(delete("/pipeline/1")
                .contentType("application/x-yaml")
                .accept(MediaType.parseMediaType(APPLICATION_YAML_UTF_8)))
                .andExpect(status().isOk());
    }

    @Test
    public void test_get_successRequest() throws Exception {
        mockMvc.perform(get("/pipeline/1")
                .contentType("application/x-yaml")
                .accept(MediaType.parseMediaType(APPLICATION_YAML_UTF_8)))
                .andExpect(status().isOk());
    }

    public String getYamlPipeline () {
        return "name: name\ndescription: description\n";
    }

}
