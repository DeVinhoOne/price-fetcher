package com.devinho.pricefetcher.service;

import com.devinho.pricefetcher.mapper.EmailAlertMapper;
import com.devinho.pricefetcher.model.dto.alert.CreateEmailAlertDto;
import com.devinho.pricefetcher.model.dto.alert.CreateEmailAlertResponseDto;
import com.devinho.pricefetcher.model.dto.alert.EmailAlertRetrievalDto;
import com.devinho.pricefetcher.repository.EmailAlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailAlertService {

    private final EmailAlertRepository emailAlertRepository;

    public CreateEmailAlertResponseDto createNewEmailAlert(CreateEmailAlertDto dto) {
        log.info(dto.toString());
        var emailAlert = EmailAlertMapper.mapCreateEmailAlertDtoToEntity(dto);
        var savedEmailAlert = emailAlertRepository.save(emailAlert);
        log.info("Saved email alert [alertId: {}]", savedEmailAlert.getAlertId());
        return EmailAlertMapper.mapEntityToCreateEmailAlertResponseDto(savedEmailAlert);
    }

    public Query getAlertsByEmail(String email) {
        List<EmailAlertRetrievalDto> emailAlertDtos = emailAlertRepository.getEmailAlertsByEmail(email);
        log.info("Retrieved email alerts [e-mail: {}, count: {}]", email, emailAlertDtos.size());
        return new Query(emailAlertDtos);
    }

    public record Query(List<EmailAlertRetrievalDto> emailAlerts) {}
}


