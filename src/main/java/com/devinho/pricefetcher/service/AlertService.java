package com.devinho.pricefetcher.service;

import com.devinho.pricefetcher.mapper.EmailAlertMapper;
import com.devinho.pricefetcher.model.dto.alert.CreateEmailAlertDto;
import com.devinho.pricefetcher.model.dto.alert.CreateEmailAlertResponseDto;
import com.devinho.pricefetcher.repository.EmailAlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlertService {

    private final EmailAlertRepository emailAlertRepository;

    public CreateEmailAlertResponseDto createNewEmailAlert(CreateEmailAlertDto dto) {
        log.info("Saving new email alert");
        log.info(dto.toString());
        var emailAlert = EmailAlertMapper.mapCreateEmailAlertDtoToEntity(dto);
        var savedEmailAlert = emailAlertRepository.save(emailAlert);
        log.info("Saved email alert [alertId: {}]", savedEmailAlert.getAlertId());
        return EmailAlertMapper.mapEntityToCreateEmailAlertResponseDto(savedEmailAlert);
    }
}
