package com.server.base.components.configure.security.byPass;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(value = "security_properties")
@ConfigurationProperties("security")
@Setter
public class ByPass {
    public List<String> ignorePath;

    public String[] ignoreSecurityPath(){
        return ignorePath.stream().toArray(String[]::new);
    }
}
