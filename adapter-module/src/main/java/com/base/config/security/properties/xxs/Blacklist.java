package com.base.config.security.properties.xxs;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "security.xss.restricted")
public record Blacklist(
    List<String> path
) {

    public String[] array() {
        return path.stream().toArray(String[]::new);
    }
}
