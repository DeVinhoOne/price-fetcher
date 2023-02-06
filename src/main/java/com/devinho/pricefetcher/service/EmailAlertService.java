package com.devinho.pricefetcher.service;

import com.devinho.pricefetcher.mapper.EmailAlertMapper;
import com.devinho.pricefetcher.model.dto.alert.EmailAlertCreationDto;
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
    private final EmailService emailService;

    public CreateEmailAlertResponseDto createNewEmailAlert(EmailAlertCreationDto dto) {
        var emailAlert = EmailAlertMapper.mapCreateEmailAlertDtoToEntity(dto);
        var savedEmailAlert = emailAlertRepository.save(emailAlert);
        log.info("Saved email alert [alertId: {}]", savedEmailAlert.getAlertId());
        var emailAlertDto = EmailAlertMapper.mapEntityToEmailAlertDto(savedEmailAlert);
        emailService.sendEmailWithAlertCreateConfirmation(emailAlertDto);
        return EmailAlertMapper.mapEntityToCreateEmailAlertResponseDto(savedEmailAlert);
    }

    public Result getAlertsByEmail(String email) {
        List<EmailAlertRetrievalDto> emailAlertDtos = emailAlertRepository.getEmailAlertsByEmail(email);
        log.info("Retrieved email alerts [e-mail: {}, count: {}]", email, emailAlertDtos.size());
        return new Result(emailAlertDtos);
    }

    public record Result(List<EmailAlertRetrievalDto> emailAlerts) {}
}


