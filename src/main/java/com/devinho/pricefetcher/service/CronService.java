package com.devinho.pricefetcher.service;

import com.devinho.pricefetcher.model.dto.ScrapedProductDto;
import com.devinho.pricefetcher.model.dto.alert.EmailAlertDto;
import com.devinho.pricefetcher.repository.EmailAlertRepository;
import com.devinho.pricefetcher.service.fetcher.AmazonFetcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class CronService {
    private final AmazonFetcher amazonFetcher;
    private final EmailAlertRepository emailAlertRepository;
    private final ScrapedProductRecordService scrapedProductRecordService;

    @Scheduled(fixedRate = 1_200_000)
    @Async
    public void run() {
        var cronStart = LocalDateTime.now();
        log.info("[CRON] START: {}", cronStart);
        var emailAlertDtos = emailAlertRepository.findAllDto();
        emailAlertDtos.forEach(this::getPriceForSingleAlert);
        var cronEnd = LocalDateTime.now();
        var cronDurationInSeconds = Duration.between(cronStart, cronEnd).toSeconds();
        log.info("[CRON] END: {}", cronEnd);
        log.info("[CRON] Duration: {} seconds", cronDurationInSeconds);
    }

    private void getPriceForSingleAlert(EmailAlertDto emailAlertDto) {
        ScrapedProductDto scrapedProductDto = switch (emailAlertDto.ecommerce()) {
            case AMAZON:
                yield amazonFetcher.fetchProductPricing(emailAlertDto.productUrl());
        };
        if (scrapedProductDto.price().value() == -1) {
            log.warn("Cannot fetch price [emailAlertId: {}, ecommerce: {}]", emailAlertDto.alertId(), emailAlertDto.ecommerce());
            return;
        }
        scrapedProductRecordService.saveNewProductData(scrapedProductDto, emailAlertDto);
    }
}