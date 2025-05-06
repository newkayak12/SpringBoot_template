package com.base.config.mvc;

import com.base.config.i18n.HttpHeaderLocaleResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final LocalValidatorFactoryBean validatorFactoryBean;

    @Bean
    public LocaleResolver localeResolver() {
        return new HttpHeaderLocaleResolver();
    }

    @Override
    public Validator getValidator() {
        return validatorFactoryBean;
    }
}
