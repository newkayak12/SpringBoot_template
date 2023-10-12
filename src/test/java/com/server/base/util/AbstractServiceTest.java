package com.server.base.util;

import com.server.base.components.constants.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
@EnableConfigurationProperties
public abstract class AbstractServiceTest {
    @Spy
    protected ModelMapper mapper;

    @Spy
    protected BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    protected void setProperties() {
        Constants constants = new Constants();
        try {
            List<Method> methods = Arrays.stream(Constants.class.getMethods())
                    .filter(method -> Arrays.stream(method.getAnnotations())
                            .anyMatch(annotation -> annotation.annotationType().equals(Value.class))
                    )
                    .collect(Collectors.toList());

            for ( Method method : methods ) {
                ReflectionUtils.invokeMethod(method, constants, "test");
            }
        } catch ( Exception e ){
            e.printStackTrace();
        }

    }

    @BeforeEach
    protected void setModelMapperSet () {
        this.mapper.getConfiguration()
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true);
    }
}
