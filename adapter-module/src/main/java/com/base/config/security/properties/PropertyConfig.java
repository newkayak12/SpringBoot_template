package com.base.config.security.properties;

import com.base.config.security.properties.jwt.JwtProperties;
import com.base.config.security.properties.jwt.Whitelist;
import com.base.config.security.properties.xxs.Blacklist;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = {Whitelist.class, JwtProperties.class, Blacklist.class})
public class PropertyConfig {

}
