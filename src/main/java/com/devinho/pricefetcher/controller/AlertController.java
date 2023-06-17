package com.devinho.pricefetcher.controller;

import com.devinho.pricefetcher.model.dto.alert.CreateEmailAlertResponseDto;
import com.devinho.pricefetcher.model.dto.alert.EmailAlertCreationDto;
import com.devinho.pricefetcher.service.EmailAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.devinho.pricefetcher.service.EmailAlertService.Result;

@RestController
@RequiredArgsConstructor
public class AlertController {

    private final EmailAlertService emailAlertService;

    @PostMapping("/alerts/create") //TODO 1. Add validation
    public CreateEmailAlertResponseDto createNewEmailAlert(@RequestBody EmailAlertCreationDto dto) {
        return emailAlertService.createNewEmailAlert(dto);
    }

    @GetMapping("/alerts/")
    public Result getAlertsByEmail(@RequestHeader String email) {
        return emailAlertService.getAlertsByEmail(email);
    }

    @DeleteMapping("/alerts/delete/{strAlertId}")
    public void deleteAlertById(@PathVariable String strAlertId) {
        emailAlertService.deleteAlertById(strAlertId);
    }
}
