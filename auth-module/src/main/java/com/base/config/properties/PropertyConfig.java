package com.base.config.properties;

import com.base.config.properties.jwt.JwtProperties;
import com.base.config.properties.jwt.Whitelist;
import com.base.config.properties.xxs.Blacklist;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = {Whitelist.class, JwtProperties.class, Blacklist.class})
public class PropertyConfig {

}
