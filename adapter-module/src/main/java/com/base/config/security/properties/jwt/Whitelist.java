package com.base.config.security.properties.jwt;


import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "security.jwt.allowed")
public record Whitelist(
    List<String> path
) {

    public String[] array() {
        return path.stream().toArray(String[]::new);
    }
}
