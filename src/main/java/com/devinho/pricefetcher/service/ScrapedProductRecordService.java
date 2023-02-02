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
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScrapedProductRecordService {

    private final ScrapedProductRecordRepository scrapedProductRecordRepository;
    private final EmailAlertRepository emailAlertRepository;
    private final EmailService emailService;

    @Transactional
    public void saveNewProductData(ScrapedProductDto newScrapedProductData, com.devinho.pricefetcher.model.dto.alert.EmailAlertDto emailAlertDto) {
        var scrapedProductRecordOpt = scrapedProductRecordRepository.findById(emailAlertDto.alertId());
        if (scrapedProductRecordOpt.isEmpty()) saveNewScrapedProductRecord(newScrapedProductData, emailAlertDto);
        else updateExistingProductRecord(scrapedProductRecordOpt.get(), newScrapedProductData, emailAlertDto);
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
        newScrapedProductRecordEntity.setUpdatedAt(currentDateTime);

        var savedEntity = scrapedProductRecordRepository.save(newScrapedProductRecordEntity);
        log.info("Save entity ScrapedProductRecord [id: {}]", savedEntity.getId());
    }

    private void updateExistingProductRecord(ScrapedProductRecord existingEntity,
                                             ScrapedProductDto newScrapedProductData,
                                             EmailAlertDto emailAlertDto) {
        if (Objects.equals(existingEntity.getLastPrice(), newScrapedProductData.price().value())) return;
        var oldPrice = existingEntity.getLastPrice();
        log.info("Price change, entity update ScrapedProductRecord [id: {}, oldPrice: {}, newPrice: {}]",
                existingEntity.getId(), oldPrice, newScrapedProductData.price().value());
        existingEntity.setLastPrice(newScrapedProductData.price().value());
        existingEntity.setUpdatedAt(LocalDateTime.now());
        scrapedProductRecordRepository.save(existingEntity);
        LocalDateTime sentAt = emailService.sendEmailWithPriceChangeInfo(emailAlertDto, newScrapedProductData, oldPrice);
        emailAlertRepository.updateLastAlertSentAtById(emailAlertDto.alertId(), sentAt);
    }
}
