package com.devinho.pricefetcher.controller;

import com.devinho.pricefetcher.model.dto.alert.EmailAlertCreationDto;
import com.devinho.pricefetcher.model.dto.alert.CreateEmailAlertResponseDto;
import com.devinho.pricefetcher.service.EmailAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.devinho.pricefetcher.service.EmailAlertService.*;

@RestController
@RequestMapping("/alerts")
@RequiredArgsConstructor
public class AlertContoller {

    private final EmailAlertService emailAlertService;

    @PostMapping("/create") //TODO 1. Add validation
    public CreateEmailAlertResponseDto createNewEmailAlert(@RequestBody EmailAlertCreationDto dto) {
        return emailAlertService.createNewEmailAlert(dto);
    }

    @GetMapping("/")
    public Query getAlertsByEmail(@RequestHeader String email) {
        return emailAlertService.getAlertsByEmail(email);
    }
}
