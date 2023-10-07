package com.server.base.components.configure.swagger;

import com.server.base.components.configure.ConfigMsg;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.PostConstruct;

@Configuration(value = "swagger_configuration")
@DependsOn(value = {"constants"})
public class Config{
    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .components(
                        new Components()
                                .addSecuritySchemes("bearer-key",
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                )
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Base Server Swagger")
                .description("Base Server Swagger")
                .version("1.0");
    }


    @PostConstruct
    public void enabled(){
        ConfigMsg.msg("Swagger 3.0");
    }
}
