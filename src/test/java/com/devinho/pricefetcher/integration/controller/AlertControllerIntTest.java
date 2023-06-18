package com.devinho.pricefetcher.integration.controller;

import com.devinho.pricefetcher.model.SupportedEcommerce;
import com.devinho.pricefetcher.model.dto.alert.CreateEmailAlertResponseDto;
import com.devinho.pricefetcher.model.dto.alert.EmailAlertCreationDto;
import com.devinho.pricefetcher.model.dto.alert.EmailAlertDto;
import com.devinho.pricefetcher.model.dto.alert.EmailAlertRetrievalDto;
import com.devinho.pricefetcher.service.EmailAlertService;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
//TODO replace in future (long execution times)
public class AlertControllerIntTest {

    private static final String TEST_MAIL = "test@mail.com";
    private static List<EmailAlertCreationDto> emailAlertCreationDtos;

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
        setupEmailAlertCreationDtos();
    }

    @Test
    @SneakyThrows
    void whenValidEmailAlertCreate_shouldReturn200() {
        //GIVEN
        var emailAlertCreateDto = emailAlertCreationDtos.get(0);
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

    @Test
    @SneakyThrows
    void whenThreeEmailAlertsAreCreated_ThenWhenFetchAlertsListContainsThreeElements() {
        //WHEN
        for (var emailAlertCreationDto : emailAlertCreationDtos) {
            var jsonRequestBody = objectMapper.writeValueAsString(emailAlertCreationDto);
            mockMvc.perform(post("/alerts/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRequestBody)).andExpect(status().isOk());
        }
        var resultActions = mockMvc.perform(get("/alerts/").header("email", TEST_MAIL)).andExpect(status().isOk());
        var result = objectMapper
                .readValue(resultActions.andReturn().getResponse().getContentAsString(), EmailAlertService.Result.class);
        List<EmailAlertRetrievalDto> emailAlertRetrievalDtos = result.emailAlerts();
        //THEN
        Assertions.assertEquals(3, emailAlertRetrievalDtos.size());
    }

    @Test
    @SneakyThrows
    void whenNonexistentAlertDelete_ThenAppropriateErrorReturn() {
        //WHEN
        var resultActions = mockMvc.perform(delete("/alerts/delete/{alertId}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
        var map = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), Map.class);
        //THEN
        Assertions.assertEquals("AlertNotFoundException", map.get("error"));
        Assertions.assertEquals("Entity does not exists", map.get("message"));
    }

    @Test
    @SneakyThrows
    void whenCorrectAlertDelete_ThenReturn200AndNoAlertsAvailable() {
        //GIVEN
        var emailAlertCreateDto = emailAlertCreationDtos.get(0);
        var jsonRequestBody = objectMapper.writeValueAsString(emailAlertCreateDto);
        mockMvc.perform(post("/alerts/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequestBody));
        var resultActions = mockMvc.perform(get("/alerts/").header("email", TEST_MAIL));
        var getAlertsResult = objectMapper
                .readValue(resultActions.andReturn().getResponse().getContentAsString(), EmailAlertService.Result.class);
        var createdAlertId = getAlertsResult.emailAlerts().get(0).alertId();
        //WHEN
        mockMvc.perform(delete("/alerts/delete/{alertId}", createdAlertId))
                .andExpect(status().isOk());
        //THEN
        var resultActionsAfterDelete = mockMvc.perform(get("/alerts/").header("email", TEST_MAIL));
        var getAlertsResultAfterDelete = objectMapper
                .readValue(resultActionsAfterDelete.andReturn().getResponse().getContentAsString(), EmailAlertService.Result.class);
        Assertions.assertEquals(0, getAlertsResultAfterDelete.emailAlerts().size());
    }

    @Test
    @SneakyThrows
    void whenNotValidUUIDForAlertDelete_ThenAppropriateErrorReturn() {
        //WHEN
        var resultActions = mockMvc.perform(delete("/alerts/delete/{alertId}", "THIS-IS-NOT-UUID"))
                .andExpect(status().isBadRequest());
        var map = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), Map.class);
        //THEN
        Assertions.assertEquals("GenericPriceFetcherException", map.get("error"));
        Assertions.assertEquals("Provided ID is not valid UUID", map.get("message"));
    }

    @SneakyThrows
    private void setupEmailAlertCreationDtos() {
        emailAlertCreationDtos = List.of(
                new EmailAlertCreationDto(
                        "New alert name 1",
                        "New alert description 1",
                        TEST_MAIL,
                        new URL("https://www.amazon.pl/Anker-PowerPort-3-portowa-ladowarka-kompaktowy/dp/B09LLRNGSD"),
                        SupportedEcommerce.AMAZON
                ),
                new EmailAlertCreationDto(
                        "New alert name 2",
                        "New alert description 2",
                        TEST_MAIL,
                        new URL("https://www.amazon.pl/Anker-PowerPort-3-portowa-ladowarka-kompaktowy/dp/B09LLRNGSD"),
                        SupportedEcommerce.AMAZON
                ),
                new EmailAlertCreationDto(
                        "New alert name 3",
                        "New alert description 3",
                        TEST_MAIL,
                        new URL("https://www.amazon.pl/Anker-PowerPort-3-portowa-ladowarka-kompaktowy/dp/B09LLRNGSD"),
                        SupportedEcommerce.AMAZON
                )
        );
    }
}
