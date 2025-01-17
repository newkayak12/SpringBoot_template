package com.base.config.properties.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "security.jwt.properties")
public record JwtProperties(
    Long expireTime,
    String secret,
    String issuer


) {

}
