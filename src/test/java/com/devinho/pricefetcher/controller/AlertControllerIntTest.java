package com.devinho.pricefetcher.controller;

import com.devinho.pricefetcher.model.SupportedEcommerce;
import com.devinho.pricefetcher.model.dto.alert.CreateEmailAlertResponseDto;
import com.devinho.pricefetcher.model.dto.alert.EmailAlertCreationDto;
import com.devinho.pricefetcher.model.dto.alert.EmailAlertDto;
import com.devinho.pricefetcher.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URL;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AlertControllerIntTest {

    @MockBean
    private EmailService emailService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    void setup() {
        //DISABLE SENDING AN EMAIL
        doNothing().when(emailService).sendEmailWithAlertCreateConfirmation(any(EmailAlertDto.class));
    }

    @Test
    @SneakyThrows
    void whenValidEmailAlertCreate_shouldReturn200() {
        //GIVEN
        var emailAlertCreateDto = new EmailAlertCreationDto(
                "New alert name",
                "New alert description",
                "tets1@mail.com",
                new URL("https://www.amazon.pl/Anker-PowerPort-3-portowa-ladowarka-kompaktowy/dp/B09LLRNGSD"),
                SupportedEcommerce.AMAZON);
        var jsonRequestBody = objectMapper.writeValueAsString(emailAlertCreateDto);
        //WHEN
        var resultActions = mockMvc.perform(post("/alerts/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody)).andExpect(status().isOk());
        var createEmailAlertResponseDto = objectMapper
                .readValue(resultActions.andReturn().getResponse().getContentAsString(), CreateEmailAlertResponseDto.class);
        //THEN
        Assertions.assertEquals(emailAlertCreateDto.alertName(), createEmailAlertResponseDto.alertName());
        Assertions.assertInstanceOf(LocalDateTime.class, createEmailAlertResponseDto.alertCreatedAt());
    }

}
