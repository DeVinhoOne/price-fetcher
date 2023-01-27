package com.devinho.pricefetcher.mapper;

import com.devinho.pricefetcher.model.dto.alert.CreateEmailAlertDto;
import com.devinho.pricefetcher.model.dto.alert.CreateEmailAlertResponseDto;
import com.devinho.pricefetcher.model.entity.EmailAlert;

import java.time.LocalDateTime;

public class EmailAlertMapper {
    public static EmailAlert mapCreateEmailAlertDtoToEntity(CreateEmailAlertDto dto) {
        var emailAlert = new EmailAlert();
        emailAlert.setEmail(dto.email());
        emailAlert.setProductUrl(dto.productUrl());
        emailAlert.setAlertName(dto.alertName());
        emailAlert.setAlertDescription(dto.alertDescription());
        emailAlert.setEcommerce(dto.ecommerce());
        emailAlert.setAlertCreatedAt(LocalDateTime.now());
        emailAlert.setLastAlertSentAt(null);
        return emailAlert;
    }

    public static CreateEmailAlertResponseDto mapEntityToCreateEmailAlertResponseDto(EmailAlert emailAlert) {
        return new CreateEmailAlertResponseDto(emailAlert.getAlertName(), emailAlert.getAlertCreatedAt());
    }
}
