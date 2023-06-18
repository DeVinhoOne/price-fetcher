package com.devinho.pricefetcher.service;

import com.devinho.pricefetcher.mapper.ScrapedProductRecordMapper;
import com.devinho.pricefetcher.model.dto.ScrapedProductDto;
import com.devinho.pricefetcher.model.dto.alert.EmailAlertDto;
import com.devinho.pricefetcher.model.entity.ScrapedProductRecord;
import com.devinho.pricefetcher.repository.EmailAlertRepository;
import com.devinho.pricefetcher.repository.ScrapedProductRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScrapedProductRecordService {

    private final ScrapedProductRecordRepository scrapedProductRecordRepository;
    private final EmailAlertRepository emailAlertRepository;
    private final EmailService emailService;

    @Transactional
    public void saveNewProductData(ScrapedProductDto newProductData, EmailAlertDto emailAlertDto) {
        Optional<ScrapedProductRecord> existingProductData = scrapedProductRecordRepository.findLatestByEmailAlertId(emailAlertDto.alertId());
        saveNewScrapedProductRecord(newProductData, emailAlertDto);
        existingProductData
                .ifPresent(scrapedProductRecord -> checkIfAlertShouldBeSent(scrapedProductRecord, newProductData, emailAlertDto));
    }

    private void saveNewScrapedProductRecord(ScrapedProductDto scrapedProductDto, EmailAlertDto emailAlertDto) {
        var currentDateTime = LocalDateTime.now();
        var emailAlertOpt = emailAlertRepository.findById(emailAlertDto.alertId());
        var emailAlertEntity = emailAlertOpt.orElseThrow(
                () -> new RuntimeException("Unable to find EmailAlert [id: {" + emailAlertDto.alertId() + "}]"));
        var newScrapedProductRecordEntity = ScrapedProductRecordMapper
                .mapScrapedProductDtoToScrapedProductRecord(scrapedProductDto);
        newScrapedProductRecordEntity.setEmailAlert(emailAlertEntity);
        newScrapedProductRecordEntity.setCreatedAt(currentDateTime);
        var savedEntity = scrapedProductRecordRepository.save(newScrapedProductRecordEntity);
        log.info("Save entity ScrapedProductRecord [id: {}]", savedEntity.getRecordId());
    }

    private void checkIfAlertShouldBeSent(ScrapedProductRecord existingLatestProductData, ScrapedProductDto newProductData, EmailAlertDto emailAlertDto) {
        if (newProductData.price().value() < existingLatestProductData.getLastPrice())
            emailService.sendEmailWithPriceChangeInfo(emailAlertDto, newProductData, existingLatestProductData.getLastPrice());
    }
}
