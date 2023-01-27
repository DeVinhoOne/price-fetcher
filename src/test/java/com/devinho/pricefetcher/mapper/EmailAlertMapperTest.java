package com.devinho.pricefetcher.mapper;

import com.devinho.pricefetcher.model.SupportedEcommerce;
import com.devinho.pricefetcher.model.dto.alert.CreateEmailAlertDto;
import com.devinho.pricefetcher.model.dto.alert.CreateEmailAlertResponseDto;
import com.devinho.pricefetcher.model.entity.EmailAlert;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EmailAlertMapperTest {

    @Test
    void mapCreateEmailAlertDtoToEntity() throws MalformedURLException {
        var alertName = "Test alert name";
        var alertDescription = "Test alert description";
        var email = "pricefetcher@test.com";
        var productUrl = new URL("https://google.com/");
        var ecommerce = SupportedEcommerce.AMAZON;
        var dto = new CreateEmailAlertDto(alertName, alertDescription, email, productUrl, ecommerce);
        var entity = EmailAlertMapper.mapCreateEmailAlertDtoToEntity(dto);
        assertEquals(alertName, entity.getAlertName());
        assertEquals(alertDescription, entity.getAlertDescription());
        assertEquals(email, entity.getEmail());
        assertEquals(productUrl, entity.getProductUrl());
        assertEquals(ecommerce, entity.getEcommerce());
    }

    @Test
    void mapEntityToCreateEmailAlertResponseDto() {
        var alertName = "Test alert name";
        var alertCreatedAt = LocalDateTime.now();
        EmailAlert emailAlert = new EmailAlert();
        emailAlert.setAlertName(alertName);
        emailAlert.setAlertCreatedAt(alertCreatedAt);
        var dto = EmailAlertMapper.mapEntityToCreateEmailAlertResponseDto(emailAlert);
        assertEquals(alertName, dto.alertName());
        assertEquals(alertCreatedAt, dto.alertCreatedAt());
    }
}