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
    private final String checkId = prefix+"/check/id/";
    private final String signUp = prefix+"/sign/up";
    private final String changePassword = prefix+"/change/password";
    private final String findId = prefix+"/find/id/";
    private final String findPassword = prefix+"/find/password/";


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

//

    @Nested
    @DisplayName("아이디 중복 체크 테스트")
    class CheckDuplicatedId{

        @Test
        @DisplayName(value = "중복")
        public void duplicated() throws Exception {
            mockMvc.perform(
                            get(checkId+"test")
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().string("false"))
                    .andDo(print());
        }

        @Test
        @DisplayName(value = "비중복")
        public void notDuplicated() throws Exception {
            Random rand = new Random();
            mockMvc.perform(
                            get(checkId+"test"+rand.nextInt(200))
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().string("true"))
                    .andDo(print());
        }
    }

//

    @Test
    @DisplayName(value = "회원 가입")
    @Transactional
    @Rollback(value = true)
    public void signUpTest() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUserId("test");
        signUpRequest.setUserPwd("1212");
        signUpRequest.setMobileNo("01012341234");
        signUpRequest.setEmail("test@test.com");

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

//

    @Test
    @DisplayName(value = "비밀번호 변경 테스트")
    @Transactional
    @Rollback(value = true)
    public void changePasswordTest() throws Exception {
        mockMvc.perform(
                patch(changePassword)
                    .param("userId", "test")
                    .param("userPwd", "1212")
                    .param("newUserPwd", "1313")
        )
        .andExpect(status().isOk())
        .andExpect(content().string("true"))
        .andDo(print());
    }

    @Nested
    @DisplayName(value = "아이디 찾기")
    public class FindId {
        @Test
        @DisplayName(value = "성공")
        public void success() throws Exception {
            mockMvc.perform(
                    get(findId)
                    .param("email", "test@test.com")
                    .param("mobileNo", "01012341234")
            )
            .andExpect(status().isOk())
            .andExpect(content().string("test"))
            .andDo(print());
        }

        @Test
        @DisplayName(value = "실패")
        public void failure() throws Exception {
            mockMvc.perform(
                            get(findId)
                                    .param("email", "test12@test.com")
                                    .param("mobileNo", "01012341234")
                    )
                    .andExpect(status().is5xxServerError())
                    .andExpect(content().string(BecauseOf.NO_DATA.getMsg()))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName(value = "비밀번호 찾기")
    public class FindPassword {
        @DisplayName(value = "성공")
        @Test
        @Transactional
        @Rollback
        public void success () throws Exception {
            mockMvc.perform(
                put(findPassword)
                .queryParam("userId", "test")
                .queryParam("email", "newkayak12@gmail.com")
                .queryParam("mobileNo", "01012341234")
            )
            .andExpect(status().isOk())
            .andExpect(content().string("true"));
        }

        @DisplayName(value = "실패")
        @Test
        public void failure () throws Exception {
            mockMvc.perform(
                            put(findPassword)
                                    .queryParam("userId", "test")
                                    .queryParam("email", "test@test.com")
                                    .queryParam("mobileNo", "01012341234")
                    )
                    .andExpect(status().is5xxServerError())
                    .andExpect(content().string(BecauseOf.NO_DATA.getMsg()));
        }
    }
}
