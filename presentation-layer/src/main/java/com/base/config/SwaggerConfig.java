package com.base.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Component
@Slf4j
public class SwaggerConfig {

    private SecurityScheme getSecurityScheme() {
        return new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("Authorization")
            .in(SecurityScheme.In.HEADER)
            .name(HttpHeaders.AUTHORIZATION);
    }

    private Components getComponents() {
        return new Components().addSecuritySchemes("Authorization", this.getSecurityScheme());
    }

    @Bean
    public OpenAPI api() {
        SecurityRequirement addSecurityItem = new SecurityRequirement();


        addSecurityItem.addList("Authorization");
        return new OpenAPI()
            .components(this.getComponents())
            .addSecurityItem(addSecurityItem)
            .info(apiInfo());
    }



    private Info apiInfo() {
        return new Info()
            .title("base")
            .description("base")
            .version("1.0.0");
    }

}
