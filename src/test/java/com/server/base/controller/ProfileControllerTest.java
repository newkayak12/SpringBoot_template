package com.server.base.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.base.components.exceptions.BecauseOf;
import com.server.base.util.AbstractControllerTest;
import com.server.base.util.TestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
@EnableConfigurationProperties
@ActiveProfiles(value = {"local"})
public class ProfileControllerTest extends AbstractControllerTest {
    private final String prefix = "/api/v1/profile";
    @Spy
    private ModelMapper mapper;
    @Spy
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName(value = "프로필")
    class Profiles {

        @Test
        @DisplayName(value = "단일")
        public void mono () throws Exception {
            mockMvc.perform(
                patch(String.format("%s/%d", prefix, 18))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.profileNo").value(18))
            .andExpect(jsonPath("$.isPush").isNotEmpty())
            .andExpect(jsonPath("$.profileName").value("PROFILE_NUMBER_1"))
            .andExpect(jsonPath("$.image").isNotEmpty());

        }

        @Test
        @DisplayName(value = "리스트 없음")
        public void empty() throws Exception {
            mockMvc.perform(
                    get(prefix+"/")
                    .param("userNo","22")
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isEmpty());
        }

        @Test
        @DisplayName(value = "리스트 가져오기")
        public void list() throws Exception {
            mockMvc.perform(
                            get(prefix+"/")
                                    .param("userNo","23")
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(4))

                    .andExpect(jsonPath("$[0].profileNo").isNotEmpty())
                    .andExpect(jsonPath("$[1].profileNo").isNotEmpty())
                    .andExpect(jsonPath("$[2].profileNo").isNotEmpty())
                    .andExpect(jsonPath("$[3].profileNo").isNotEmpty())

                    .andExpect(jsonPath("$[0].profileName").value("profile_1"))
                    .andExpect(jsonPath("$[1].profileName").value("profile_2"))
                    .andExpect(jsonPath("$[2].profileName").value("profile_3"))
                    .andExpect(jsonPath("$[3].profileName").value("profile_4"))

                    .andExpect(jsonPath("$[0].image").isNotEmpty())
                    .andExpect(jsonPath("$[1].image").isNotEmpty())
                    .andExpect(jsonPath("$[2].image").isNotEmpty())
                    .andExpect(jsonPath("$[3].image").isNotEmpty());

        }
    }

    @Nested
    @DisplayName(value = "프로필 생성")
    public class SaveProfile {
        @Test
        @DisplayName(value = "성공")
        public void success() throws Exception {
            Map<String,Object> param = Map.of(
                    "profileName", "test1",
                    "account", Map.of("userNo",  22L)
            );

            ProfileSaveRequest request = mapper.map(param, ProfileSaveRequest.class);

            mockMvc.perform(
                multipart(prefix+"/save")
                .file(TestUtil.getMockMultiPartFile("/Users/sanghyeonkim/Downloads/R1280x0.png", "image", "rawFile"))
                .param("profileName", "test1")
                .param("account.userNo", "22")
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isNotEmpty())
            .andExpect(jsonPath("$").isNotEmpty())
            .andExpect(jsonPath("$.profileName").value("test1"))
            .andExpect(jsonPath("$.isPush").value("true"))
            .andExpect(jsonPath("$.image").isNotEmpty());

        }

        @ParameterizedTest
        @DisplayName(value = "성공")
        @ValueSource(longs = {1,2,3,4})
        public void parameterized(Long numbering) throws Exception {

            mockMvc.perform(
                            multipart(prefix+"/save")
                                    .file(TestUtil.getMockMultiPartFile("/Users/sanghyeonkim/Downloads/R1280x0.png", "image", "rawFile"))
                                    .param("profileName", String.format("%s_%d","profile", numbering))
                                    .param("account.userNo", "23")
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.profileNo").isNotEmpty())
                    .andExpect(jsonPath("$.profileName").value(String.format("%s_%d","profile", numbering)))
                    .andExpect(jsonPath("$.isPush").value("true"))
                    .andExpect(jsonPath("$.image").isNotEmpty());

        }
        @Test
        @DisplayName(value = "실패")
        public void failure() throws Exception {
            mockMvc.perform(
                    multipart(prefix+"/save")
                            .file(TestUtil.getMockMultiPartFile("/Users/sanghyeonkim/Downloads/R1280x0.png", "image", "rawFile"))
                            .param("profileName", "test0")
                            .param("account.userNo", "22")
            )
            .andExpect(status().is5xxServerError())
            .andExpect(jsonPath("$").value(BecauseOf.EXCEED_MAXIMUM_PROFILE_COUNT.getMsg()));
        }

    }

    @Nested
    @DisplayName(value = "프로필 수정")
    public class ModifyProfile {
        @Test
        @DisplayName(value = "프로필 이름 수정")
        public void changeProfileName() throws Exception {
            mockMvc.perform(
                patch(String.format("%s/%d/profileName", prefix, 18))
                .param("profileName", "PROFILE_NUMBER_1")
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(true));
        }
        @DisplayName(value = "프로필 푸시 수정")
        @ParameterizedTest
        @ValueSource(booleans = {true, false})
        public void changePushStatus(Boolean state) throws Exception {
            mockMvc.perform(
                patch(String.format("%s/%d/isPush", prefix, 18))
                .param("isPush", state.toString())
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(true));

        }


        @Test
        @DisplayName(value = "프로필 이미지 수정")
        public void changeProfileImage() throws Exception {
            mockMvc.perform(
                multipart(String.format("%s/%d/profileImage", prefix, 18))
                .file(TestUtil.getMockMultiPartFile("/Users/sanghyeonkim/Downloads/R1280x0.png", "image", "rawFile"))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").exists());

        }

    }

    @Nested
    @DisplayName(value = "프로필 삭제")
    public class DeleteProfile {
        @ParameterizedTest
        @DisplayName(value = "성공")
        @ValueSource(longs = {9L, 8L, 7L, 6L})
        public void success (Long no) throws Exception {
            mockMvc.perform(
                    delete(String.format("%s/%d", prefix, no))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(true));
        }
    }
}
