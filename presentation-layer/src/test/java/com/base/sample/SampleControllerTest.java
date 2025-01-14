package com.base.sample;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.base.config.URL.Sample;
import com.base.controller.SampleController;
import com.base.restdoc.Body;
import com.base.restdoc.RestDoc;
import com.base.sample.dto.request.InsertRequest;
import com.base.sample.dto.request.UpdateRequest;
import com.base.sample.dto.response.SampleResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FailoverIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.plugin.SimpleValueJqwikPlugin;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = SampleController.class)
@AutoConfigureRestDocs
public class SampleControllerTest {

    @MockBean
    private SampleService service;

    @Autowired
    private MockMvc mockMvc;
    @Spy
    private ObjectMapper objectMapper;

    @DisplayName("저장")
    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class Save {

        private FixtureMonkey fixtureMonkey;

        @BeforeAll
        void setFixtureMonkey() {
            fixtureMonkey = FixtureMonkey.builder().defaultNotNull(Boolean.TRUE)
                .plugin(new SimpleValueJqwikPlugin())
                .plugin(new JakartaValidationPlugin())
                .objectIntrospector(
                    new FailoverIntrospector(
                        List.of(
                            FieldReflectionArbitraryIntrospector.INSTANCE,
                            ConstructorPropertiesArbitraryIntrospector.INSTANCE
                        ),
                        Boolean.FALSE
                    )
                )
                .build();
        }


        @DisplayName("등록")
        @Test
        void insert() throws Exception {
            final String url = Sample.FIND_ALL;
            //given
            InsertRequest insertRequest = fixtureMonkey.giveMeOne(InsertRequest.class);

            //when
            doReturn(Boolean.TRUE).when(service).insert(any(InsertRequest.class));

            mockMvc.perform(post(url).header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(insertRequest)))
                .andDo(print())

                //then
                .andExpect(status().isOk()).andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.result").isBoolean()).andExpect(jsonPath("$.result").value(Boolean.TRUE))
                //documentations
                .andDo(
                    RestDoc.builder()
                        .identifier("api/sample/post")
                        .tag("Sample")
                        .summary("샘플 저장")
                        .withRequestFields(
                            Body.of("sampleName", JsonFieldType.STRING, false, "샘플 이름")
                        )
                        .withResponseFields(
                            Body.of("result", JsonFieldType.BOOLEAN, false, "결과")
                        )
                        .build()
                );

        }

        @DisplayName("수정")
        @Test
        void update() throws Exception {

            final String url = Sample.UPDATE.replace(":\\d+", "");
            //given

            Long id = 1L;
            UpdateRequest updateRequest = fixtureMonkey.giveMeOne(UpdateRequest.class);

            //when
            doReturn(Boolean.TRUE).when(service).update(anyLong(), any(UpdateRequest.class));

            mockMvc.perform(
                    patch(url, id)
                        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest))
                )
                .andDo(print())

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.result").isBoolean())
                .andExpect(jsonPath("$.result").value(Boolean.TRUE))
                //documentations
                .andDo(
                    RestDoc.builder()
                        .identifier("api/sample/id/patch")
                        .tag("Sample").summary("샘플 수정")
                        .withRequestFields(
                            Body.of("sampleName", JsonFieldType.STRING, false, "샘플 이름")
                        )
                        .withResponseFields(
                            Body.of("result", JsonFieldType.BOOLEAN, false, "결과")
                        )
                        .build()
                );

        }
    }

    @DisplayName("전체 조회")
    @Test
    void findAll() throws Exception {
        final String URL = Sample.FIND_ALL;

        FixtureMonkey fixtureMonkey = FixtureMonkey.builder().defaultNotNull(Boolean.TRUE)
            .plugin(new SimpleValueJqwikPlugin())
            .plugin(new JakartaValidationPlugin())
            .objectIntrospector(
                new FailoverIntrospector(
                    List.of(
                        FieldReflectionArbitraryIntrospector.INSTANCE,
                        ConstructorPropertiesArbitraryIntrospector.INSTANCE
                    ),
                    Boolean.FALSE
                )
            )
            .build();

        List<SampleResponse> fixture = fixtureMonkey.giveMe(SampleResponse.class, 10);

        doReturn(fixture).when(service).findAll();

        mockMvc.perform(
            get(URL)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").exists())
        .andExpect(jsonPath("$").isArray())
        .andDo(
            RestDoc
                .builder()
                .identifier("v1/sample/get")
                .tag("Sample")
                .summary("전체 리스트 조회")
                .withResponseFields(
                    Body.of("[].sampleId", JsonFieldType.NUMBER, false, "ID"),
                    Body.of("[].sampleName", JsonFieldType.STRING, false, "샘플 이름")
                )
                .build()
        );
    }

}
