package com.devinho.pricefetcher.service;

import com.devinho.pricefetcher.model.dto.ScrapedProductDto;
import com.devinho.pricefetcher.model.entity.EmailAlert;
import com.devinho.pricefetcher.model.entity.ScrapedProductRecord;
import com.devinho.pricefetcher.repository.ScrapedProductRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScrapedProductRecordService {

    private final ScrapedProductRecordRepository scrapedProductRecordRepository;
    private final EmailService emailService;

    public void saveScrapedProduct(ScrapedProductDto scrapedProductDto, EmailAlert emailAlert) {
        var scrapedProductRecord = scrapedProductRecordRepository.findByEmailAlert(emailAlert);
        if (scrapedProductRecord == null) saveNewScrapedProductRecord(scrapedProductDto, emailAlert);
        else updateExistingProductRecord(scrapedProductRecord, scrapedProductDto, emailAlert);
    }

    private void saveNewScrapedProductRecord(ScrapedProductDto scrapedProductDto, EmailAlert emailAlert) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        var newEntity = new ScrapedProductRecord();
        newEntity.setLastPrice(scrapedProductDto.price().value());
        newEntity.setCurrency(scrapedProductDto.price().currency());
        newEntity.setEmailAlert(emailAlert);
        newEntity.setCreatedAt(currentDateTime);
        newEntity.setUpdatedAt(currentDateTime);
        var savedEntity = scrapedProductRecordRepository.save(newEntity);
        log.info("Save entity {}", savedEntity);
        emailService.send(emailAlert, scrapedProductDto);
    }

    private void updateExistingProductRecord(ScrapedProductRecord scrapedProductRecord, ScrapedProductDto scrapedProductDto, EmailAlert emailAlert) {
        if (Objects.equals(scrapedProductRecord.getLastPrice(), scrapedProductDto.price().value())) return;
        scrapedProductRecord.setLastPrice(scrapedProductDto.price().value());
        scrapedProductRecord.setUpdatedAt(LocalDateTime.now());
        scrapedProductRecordRepository.save(scrapedProductRecord);
        log.info("Price change, entity update [oldPrice: {}, newPrice: {}]", scrapedProductRecord.getLastPrice(), scrapedProductDto.price().value());
        emailService.send(emailAlert, scrapedProductDto);
    }
}
