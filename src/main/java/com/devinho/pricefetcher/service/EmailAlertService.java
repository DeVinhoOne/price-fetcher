package com.devinho.pricefetcher.service;

import com.devinho.pricefetcher.exception.model.AlertNotFoundException;
import com.devinho.pricefetcher.exception.model.GenericPriceFetcherException;
import com.devinho.pricefetcher.mapper.EmailAlertMapper;
import com.devinho.pricefetcher.model.dto.alert.CreateEmailAlertResponseDto;
import com.devinho.pricefetcher.model.dto.alert.EmailAlertCreationDto;
import com.devinho.pricefetcher.model.dto.alert.EmailAlertRetrievalDto;
import com.devinho.pricefetcher.repository.EmailAlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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

    public void deleteAlertById(String strAlertId) {
        try {
            var alertId = UUID.fromString(strAlertId);
            emailAlertRepository.deleteById(alertId);
        } catch (EmptyResultDataAccessException e) {
            throw new AlertNotFoundException(HttpStatus.NOT_FOUND, "Entity does not exists");
        } catch (IllegalArgumentException e) {
            throw new GenericPriceFetcherException(HttpStatus.BAD_REQUEST, "Provided ID is not valid UUID");
        }
    }

    public record Result(List<EmailAlertRetrievalDto> emailAlerts) {}
}


