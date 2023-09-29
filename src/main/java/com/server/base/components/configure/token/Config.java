package com.server.base.components.configure.token;

import com.server.base.components.configure.ConfigMsg;
import com.server.base.components.constants.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import tokenManager.TokenControl;

import javax.annotation.PostConstruct;

@Configuration(value = "token_control_configuration")
@DependsOn(value = {"constants"})
public class Config {
    @PostConstruct
    public void enabled(){
        ConfigMsg.msg("TokenControl");
    }

    @Bean
    public TokenControl tokenControl () {
        return new TokenControl(Constants.SALT, Constants.TOKEN_NAME, Constants.PROJECT_NAME);
    }
}
