package com.moneyman.pipeline.config;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration
public class YamlRequestConverter extends WebMvcConfigurerAdapter {

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new YamlResponseConverter());
    }

    private class YamlResponseConverter extends AbstractJackson2HttpMessageConverter {

        public YamlResponseConverter() {
            super(new YAMLMapper(), MediaType.parseMediaType("application/x-yaml"));
        }
    }

}
