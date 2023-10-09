package com.server.base.components.configure.security.properties.duration;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component(value = "security_token_properties")
@ConfigurationProperties("jwt")
@Setter
public class TokenDuration {

    public Durations expireTime;

    public Long getExpireTime() {
        if (Objects.isNull(this.expireTime)) return Durations.HOUR_1.getTime();
        else return this.expireTime.getTime();
    }

}
