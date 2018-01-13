package com.moneyman.pipeline.rest;

import com.moneyman.pipeline.PipelineApplication;
import com.moneyman.pipeline.data.dao.PipelineRepository;
import com.moneyman.pipeline.data.entity.Pipeline;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = PipelineApplication.class)
@WebAppConfiguration
@Transactional
public class PipelineControllerTest {

    @Autowired
    private PipelineRepository repository;

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
        Assert.assertTrue(repository.findAll().isEmpty());
        mockMvc.perform(put("/pipeline")
                .contentType("application/x-yaml")
                .content(getYamlPipeline())
                .accept(MediaType.parseMediaType(APPLICATION_YAML_UTF_8)))
                .andExpect(status().isOk());

        Assert.assertFalse(repository.findAll().isEmpty());
        Assert.assertEquals(repository.findAll().size(), 1);

        Pipeline pipeline = repository.findAll().get(0);
        Assert.assertEquals(pipeline.getTasks().size(), 3);
        Assert.assertEquals(pipeline.getTransitions().size(), 2);
    }

    @Test
    public void test_update_successRequest() throws Exception {
        Long id = repository.save(createPipeline()).getId();

        mockMvc.perform(post("/pipeline/" + id)
                .contentType("application/x-yaml")
                .content(createAnotherYamlPipeline())
                .accept(MediaType.parseMediaType(APPLICATION_YAML_UTF_8)))
                .andExpect(status().isOk());

        Pipeline pipeline = repository.getOne(id);
        Assert.assertEquals(pipeline.getName(), "another name");
        Assert.assertEquals(pipeline.getDescription(), "another description");
    }

    @Test
    public void test_delete_successRequest() throws Exception {
        Long id = repository.save(createPipeline()).getId();
        Assert.assertTrue(repository.exists(id));
        mockMvc.perform(delete("/pipeline/" + id)
                .contentType("application/x-yaml")
                .accept(MediaType.parseMediaType(APPLICATION_YAML_UTF_8)))
                .andExpect(status().isOk());
        Assert.assertFalse(repository.exists(id));
    }

    @Test
    public void test_get_successRequest() throws Exception {
        Long id = repository.save(createPipeline()).getId();
        MvcResult mvcResult = mockMvc.perform(get("/pipeline/" + id)
                .contentType("application/x-yaml")
                .accept(MediaType.parseMediaType(APPLICATION_YAML_UTF_8)))
                .andExpect(status().isOk())
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        Assert.assertTrue(content.contains(id.toString()));
        Assert.assertTrue(content.contains("some name"));
        Assert.assertTrue(content.contains("some description"));

    }

    private Pipeline createPipeline() {
        Pipeline pipeline = new Pipeline();
        pipeline.setName("some name");
        pipeline.setDescription("some description");
        return pipeline;
    }

    private String createAnotherYamlPipeline() {
        StringBuilder request = new StringBuilder();
        request.append("name: another name\n");
        request.append("description: another description\n");
        return request.toString();
    }

    private String getYamlPipeline() {
        StringBuilder request = new StringBuilder();
        request.append("---\n");
        request.append("name: name\n");
        request.append("description: description\n");
        request.append("tasks:\n");

        request.append("-\n");
        request.append(" name: build\n");
        request.append(" description: build\n");
        request.append(" action:\n");
        request.append("  type: random\n");

        request.append("-\n");
        request.append(" name: test\n");
        request.append(" description: test\n");
        request.append(" action:\n");
        request.append("  type: print\n");

        request.append("-\n");
        request.append(" name: sync\n");
        request.append(" description: sync\n");
        request.append(" action:\n");
        request.append("  type: print\n");

        request.append("transitions:\n");

        request.append("-\n");
        request.append(" build: test\n");

        request.append("-\n");
        request.append(" test: sync\n");

        return request.toString();
    }

}
