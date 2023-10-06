package com.server.base.components.configure.swagger;

import com.server.base.components.configure.ConfigMsg;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Configuration(value = "swagger_configuration")
@DependsOn(value = {"constants"})
public class Config{

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo())
                .security(securityRequirement());
    }

    private Info apiInfo() {
        return new Info()
                .title("BaseServer Swagger")
                .description("Base Server Swagger Config")
                .version("1.0");
    }

    private List<SecurityRequirement> securityRequirement() {
        SecurityRequirement securityRequirement = new SecurityRequirement();
        securityRequirement.addList("Authorization");

        return Arrays.asList(securityRequirement);

    }

    @PostConstruct
    public void enabled(){
        ConfigMsg.msg("Swagger 3.0");
    }
}
