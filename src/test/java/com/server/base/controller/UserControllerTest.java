package com.server.base.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.base.components.constants.Constants;
import com.server.base.components.exceptions.BecauseOf;
import com.server.base.repository.dto.request.SignUpRequest;
import com.server.base.util.AbstractControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Random;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest
@AutoConfigureMockMvc
@EnableConfigurationProperties
@ActiveProfiles(value = {"local"})
public class UserControllerTest extends AbstractControllerTest {

    private final String prefix = "/api/v1/user";
    private final String signIn = prefix+"/sign/in";
    private final String signUp = prefix+"/sign/up";


    @Autowired
    private ObjectMapper mapper;


    @Nested
    @DisplayName(value = "로그인 테스트")
    class SignInTest {
        @Test
        @DisplayName(value = "성공")
        public void success() throws Exception {

            mockMvc.perform(
                            get(signIn)
                                    .param("userId", "test")
                                    .param("userPwd", "1212")
                    )
                    .andExpect(status().isOk())
                    .andExpect(header().exists(Constants.TOKEN_NAME))
                    .andDo(print());

        }

        @Test
        @DisplayName(value = "실패")
        public void failure () throws Exception {
            mockMvc.perform(
                            get(signIn)
                                    .param("userId", "test")
                                    .param("userPwd", "12124")
                    )
                    .andExpect(status().is5xxServerError())
                    .andExpect(content().string(BecauseOf.ACCOUNT_NOT_EXIST.getMsg()))
                    .andDo(print());
        }
    }

    @Test
    @DisplayName(value = "회원 가입")
    @Transactional
    @Rollback(value = true)
    public void signUpTest() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUserId("test");
        signUpRequest.setUserPwd("1212");

        mockMvc.perform(
                    post(signUp)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(signUpRequest))
                )
                .andExpect(status().isOk())
                .andExpect(header().exists(Constants.TOKEN_NAME))
                .andDo(print());
    }
}
