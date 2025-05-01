package com.base.sample;


import com.base.rest.sample.SampleController;
import com.base.sample.ports.in.CreateSampleCommand;
import com.base.sample.ports.in.FindAllSampleQuery;
import com.base.sample.ports.in.UpdateSampleCommand;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@WebMvcTest(controllers = SampleController.class)
@AutoConfigureRestDocs
@ActiveProfiles(value = {"testflight"})
class SampleControllerTest {


    @MockBean
    private CreateSampleCommand createSampleCommand;

    @MockBean
    private UpdateSampleCommand updateCommand;

    @MockBean
    private FindAllSampleQuery findAllQuery;

    @Test
    void testCase() {

    }

}
