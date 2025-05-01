package com.base;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("testflight")
class BaseApplicationTest {

    private Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Autowired
    private Environment environment;

    @Test
    void contextLoad() {
        String name = environment.getProperty("spring.application.name");
        String[] profile = environment.getActiveProfiles();
        log.error("{} - {}", name, String.join(", ", profile));

        assertThat(profile).contains("testflight");
    }
}
