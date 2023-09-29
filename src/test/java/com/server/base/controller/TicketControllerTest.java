package com.server.base.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.base.components.configure.feign.ImageFeign;
import com.server.base.repository.dto.reference.AccountDto;
import com.server.base.util.AbstractControllerTest;
import com.server.base.util.TestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigInteger;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@EnableConfigurationProperties
@ActiveProfiles(value = {"local"})
public class TicketControllerTest extends AbstractControllerTest {

    private final String prefix = "/api/v1/ticket";

    private final String save = prefix + "/save";
    @Spy
    private ImageFeign imageFeign;
    @Autowired
    private ObjectMapper mapper;


    @Nested
    @DisplayName("티켓 테스트")
    public class TicketTest {
        @Test
        @DisplayName(value = "등록")
        @Transactional
        @Rollback
        public void register () throws Exception {
            InputStream inputStream = new FileInputStream("/Users/sanghyeonkim/Downloads/R1280x0.png");
            MockMultipartFile file = new MockMultipartFile("file", "file.png", "png", inputStream);


            mockMvc.perform(
                        multipart(save)
                        .file("rawFile", file.getBytes())
                        .queryParam("name", TicketType.STANDARD.name())
                        .queryParam("type", TicketType.STANDARD.name())
                        .queryParam("watchableSimultaneously", "1")
                        .queryParam("maximumResolution", "HD")
                        .queryParam("isSupportHDR", "false")
                        .queryParam("savableCount", "10")
                        .queryParam("price", "9900")
                        .contentType(MediaType.MULTIPART_FORM_DATA)

                    )
            .andExpect(content().string("true"))
            .andDo(print());
        }

        @ParameterizedTest
        @CsvFileSource(encoding = "UTF-8", numLinesToSkip = 1, files = {"/Users/sanghyeonkim/Downloads/port/netflixClone/clone_netflix_user-serivce/src/test/resources/csv/ticket/TicketRegisterTable.csv"})
        @DisplayName(value = "예시 등록")
        @Transactional
        @Rollback
        public void parameterizedRegister(
                String name, String type, Integer watchableSimultaneously,
                Resolution maximumResolution, Boolean isSupportHDR,
                Integer savableCount, BigInteger price, Boolean isActive
        ) throws Exception {

            mockMvc.perform(
                            multipart(save)
                                    .file(TestUtil.getMockMultiPartFile("/Users/sanghyeonkim/Downloads/R1280x0.png", "image", "rawFile"))
                                    .queryParam("name", name)
                                    .queryParam("type", type)
                                    .queryParam("watchableSimultaneously",watchableSimultaneously.toString())
                                    .queryParam("maximumResolution",maximumResolution.toString())
                                    .queryParam("isSupportHDR",isSupportHDR.toString())
                                    .queryParam("savableCount",savableCount.toString())
                                    .queryParam("price", price.toString())
                                    .queryParam("isActive", isActive.toString())
                                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    )
                    .andExpect(content().string("true"))
                    .andDo(print());


        }

        @Test
        @DisplayName(value = "수정")
        @Transactional
        @Rollback
        public void modify () throws Exception {
            MockMultipartFile file = TestUtil.getMockMultiPartFile("/Users/sanghyeonkim/Downloads/R1280x0.png", "image", "rawFile");
            mockMvc.perform(
                            multipart(save)
                                    .file(file)
                                    .queryParam("ticketNo", "83")
                                    .queryParam("name", TicketType.STANDARD.name())
                                    .queryParam("type", TicketType.STANDARD.name())
                                    .queryParam("watchableSimultaneously", "1")
                                    .queryParam("maximumResolution", "HD")
                                    .queryParam("isSupportHDR", "false")
                                    .queryParam("savableCount", "10")
                                    .queryParam("price", "11000")
                                    .contentType(MediaType.MULTIPART_FORM_DATA)

                    )
                    .andExpect(content().string("true"))
                    .andDo(print());
        }

        @Test
        @DisplayName(value = "삭제")
        @Transactional
        @Rollback
        public void remove () throws Exception {
            mockMvc.perform(
                delete(String.format("%s/%d", prefix, 83))
            )
            .andExpect(content().string("true"));
        }
    }

    @DisplayName(value = "조회")
    @Nested
    public class FetchTicket {

        @DisplayName(value = "전체 조회")
        @Test
        public void fetchAll() throws Exception {
            mockMvc.perform(
                get(String.format("%s/", prefix))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(4))
            .andExpect(jsonPath("$[0]").isNotEmpty())
            .andExpect(jsonPath("$[1]").isNotEmpty())
            .andExpect(jsonPath("$[2]").isNotEmpty())
            .andExpect(jsonPath("$[3]").isNotEmpty());
        }

        @DisplayName(value = "단건 조회")
        @ParameterizedTest
        @CsvFileSource(encoding = "UTF-8", numLinesToSkip = 1, files = {"/Users/sanghyeonkim/Downloads/port/netflixClone/clone_netflix_user-serivce/src/test/resources/csv/ticket/TicketFetchedTable.csv"})
        public void fetchOne(
                Long ticketNo, String name, String type, Integer watchableSimultaneously,
                String maximumResolution, Boolean isSupportHDR,
                Integer savableCount, BigInteger price, Boolean isActive
        ) throws Exception {

            mockMvc.perform(
                get(String.format("%s/%d", prefix, ticketNo))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(name))
            .andExpect(jsonPath("$.type").value(type))
            .andExpect(jsonPath("$.watchableSimultaneously").value(watchableSimultaneously))
            .andExpect(jsonPath("$.maximumResolution").value(maximumResolution))
            .andExpect(jsonPath("$.isSupportHDR").value(isSupportHDR))
            .andExpect(jsonPath("$.savableCount").value(savableCount))
            .andExpect(jsonPath("$.price").value(price))
            .andExpect(jsonPath("$.isActive").value(isActive))
            .andExpect(jsonPath("$.image").isNotEmpty());
        }
    }

    @DisplayName(value = "티켓")
    @Nested
    public class RaiseTicket {

        @DisplayName(value = "티켓 구매")
        @Test
        @Transactional
        @Rollback
        public void raiseTicket () throws Exception {
            //given
            TicketRaiseRequest request = new TicketRaiseRequest();

            //AccountDto
            AccountDto accountDto = new AccountDto();
            accountDto.setUserNo(23L);

            //TicketDto
            TicketDto ticketDto = new TicketDto();
            ticketDto.setTicketNo(84L);
            ticketDto.setName("BASIC");
            ticketDto.setType(TicketType.BASIC);
            ticketDto.setWatchableSimultaneously(1);
            ticketDto.setMaximumResolution(Resolution.HD);
            ticketDto.setIsSupportHDR(false);
            ticketDto.setSavableCount(10);
            ticketDto.setPrice(BigInteger.valueOf(9900L));
            ticketDto.setIsActive(true);

            request.setAccount(accountDto);
            request.setTicket(ticketDto);
            request.setOriginalTransaction("IMP_0000001");


            mockMvc.perform(
                post(String.format("%s/raise/ticket", prefix))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.ticketNo").value(84L))
            .andExpect(jsonPath("$.name").value("BASIC"))
            .andExpect(jsonPath("$.type").value("BASIC"))
            .andExpect(jsonPath("$.watchableSimultaneously").value(1))
            .andExpect(jsonPath("$.maximumResolution").value("HD"))
            .andExpect(jsonPath("$.isSupportHDR").value(false))
            .andExpect(jsonPath("$.savableCount").value(10))
            .andExpect(jsonPath("$.price").value(9900))
            .andExpect(jsonPath("$.isActive").value(true));
        }

        @DisplayName(value = "티켓 구매 리스트")
        @Test
        public void raises () throws Exception {
            mockMvc.perform(
                get(String.format("%s/raises", prefix))
                .param("limit", "10")
                .param("page", "1")
                .param("tableNo", "23")
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.size()").value(1));
        }


        @DisplayName(value = "다음 달 구독 변경")
        @Test
        public void toggleSubscribeStatus () throws Exception {
            mockMvc.perform(
                patch(String.format("%s/toggle/subscribe/%d", prefix, 23))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isBoolean());
        }
    }

}
