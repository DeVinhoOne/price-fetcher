package com.devinho.pricefetcher.service;

import com.devinho.pricefetcher.mapper.ScrapedProductRecordMapper;
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
        var currentDateTime = LocalDateTime.now();
        var newEntity = ScrapedProductRecordMapper.mapScrapedProductDtoToScrapedProductRecord(scrapedProductDto);
        newEntity.setEmailAlert(emailAlert);
        newEntity.setCreatedAt(currentDateTime);
        newEntity.setUpdatedAt(currentDateTime);
        var savedEntity = scrapedProductRecordRepository.save(newEntity);
        log.info("Save entity ScrapedProductRecord [id: {}]", savedEntity.getId());
        emailService.send(emailAlert, scrapedProductDto); //TODO remove it
    }

    private void updateExistingProductRecord(ScrapedProductRecord entity, ScrapedProductDto dto, EmailAlert emailAlert) {
        if (Objects.equals(entity.getLastPrice(), dto.price().value())) return;
        log.info("Price change, entity update ScrapedProductRecord [id: {}, oldPrice: {}, newPrice: {}]",
                entity.getId(), entity.getLastPrice(), dto.price().value());
        entity.setLastPrice(dto.price().value());
        entity.setUpdatedAt(LocalDateTime.now());
        scrapedProductRecordRepository.save(entity);
        emailService.send(emailAlert, dto);
    }
}
