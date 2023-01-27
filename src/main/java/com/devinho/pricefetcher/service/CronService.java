package com.devinho.pricefetcher.service;

import com.devinho.pricefetcher.model.dto.ScrapedProductDto;
import com.devinho.pricefetcher.model.entity.EmailAlert;
import com.devinho.pricefetcher.repository.EmailAlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class CronService {

    private final AmazonFetcher amazonFetcher;
    private final EmailAlertRepository emailAlertRepository;
    private final ScrapedProductRecordService scrapedProductRecordService;

    @Scheduled(fixedRate = 60_000)
    @Async
    @Transactional //TODO 2. I think it can't be transactional
    public void run() {
        var cronStart = LocalDateTime.now();
        log.info("[CRON] START: {}", cronStart);
        Iterable<EmailAlert> emailAlerts = emailAlertRepository.findAll();
        emailAlerts.forEach(this::getPriceForSingleAlert);
        var cronEnd = LocalDateTime.now();
        var cronDurationInSeconds = Duration.between(cronStart, cronEnd).toSeconds();
        log.info("[CRON] END: {}", cronEnd);
        log.info("[CRON] Duration: {} seconds", cronDurationInSeconds);
    }

    private void getPriceForSingleAlert(EmailAlert emailAlert) {
        ScrapedProductDto scrapedProductDto = switch (emailAlert.getEcommerce()) {
            case AMAZON:
                yield amazonFetcher.fetchProductPricing(emailAlert.getProductUrl());
        };
        if (scrapedProductDto.price().value() == -1) {
            log.warn("Cannot fetch price [emailAlertId: {}, ecommerce: {}]", emailAlert.getAlertId(), emailAlert.getEcommerce());
            return;
        }
        scrapedProductRecordService.saveScrapedProduct(scrapedProductDto, emailAlert);
    }
}
