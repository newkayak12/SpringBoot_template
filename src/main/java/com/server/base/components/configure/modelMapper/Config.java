package com.server.base.components.configure.modelMapper;

import com.server.base.components.configure.ConfigMsg;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration(value = "modelMapper_configuration")
public class Config {

    @PostConstruct
    public void enabled(){
        ConfigMsg.msg("ModelMapper");
    }
    private ModelMapper modelMapper;

    private void strictStrategy() {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }
    private void useReflection() {
        this.modelMapper.getConfiguration()
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true);
    }

    @Bean
    public ModelMapper modelMapper() {
        modelMapper = new ModelMapper();
        this.strictStrategy();
        this.useReflection();
        return modelMapper;
    }
}
