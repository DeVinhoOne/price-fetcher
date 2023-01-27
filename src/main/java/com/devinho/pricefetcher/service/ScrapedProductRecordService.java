package com.devinho.pricefetcher.service;

import com.devinho.pricefetcher.model.dto.ScrapedProductDto;
import com.devinho.pricefetcher.model.entity.EmailAlert;
import com.devinho.pricefetcher.model.entity.ScrapedProductRecord;
import com.devinho.pricefetcher.repository.ScrapedProductRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScrapedProductRecordService {

    private final ScrapedProductRecordRepository scrapedProductRecordRepository;

    public void saveScrapedProduct(ScrapedProductDto scrapedProductDto, EmailAlert emailAlert) {
        var scrapedProductRecord = scrapedProductRecordRepository.findByEmailAlert(emailAlert);
        if (scrapedProductRecord == null) saveNewScrapedProductRecord(scrapedProductDto, emailAlert);
        else updateExistingProductRecord(scrapedProductRecord, scrapedProductDto);
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
    }

    private void updateExistingProductRecord(ScrapedProductRecord scrapedProductRecord, ScrapedProductDto scrapedProductDto) {
        if (Objects.equals(scrapedProductRecord.getLastPrice(), scrapedProductDto.price().value())) return;
        scrapedProductRecord.setLastPrice(scrapedProductDto.price().value());
        scrapedProductRecord.setUpdatedAt(LocalDateTime.now());
        scrapedProductRecordRepository.save(scrapedProductRecord);
        log.info("Price change, entity update [oldPrice: {}, newPrice: {}]", scrapedProductRecord.getLastPrice(), scrapedProductDto.price().value());
    }
}
