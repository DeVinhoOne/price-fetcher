package com.devinho.pricefetcher.controller;

import com.devinho.pricefetcher.model.dto.alert.CreateEmailAlertDto;
import com.devinho.pricefetcher.model.dto.alert.CreateEmailAlertResponseDto;
import com.devinho.pricefetcher.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/alerts")
@RequiredArgsConstructor
public class AlertContoller {

    private final AlertService alertService;

    @PostMapping("/create") //TODO 1. Add validation
    public CreateEmailAlertResponseDto createNewEmailAlert(@RequestBody CreateEmailAlertDto dto) {
        return alertService.createNewEmailAlert(dto);
    }
}
