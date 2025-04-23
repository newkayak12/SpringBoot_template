package com.base.sample;


import com.base.rest.sample.SampleController;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;

@WebMvcTest(controllers = SampleController.class)
@AutoConfigureRestDocs
@ActiveProfiles(value = {"test"})
@PropertySource(value = {"spring.config.location=classpath:application-test.yaml"})
class SampleControllerTest {

    @Test
    void testCase() {

    }

}
