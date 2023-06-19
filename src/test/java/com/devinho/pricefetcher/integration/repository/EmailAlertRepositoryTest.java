package com.devinho.pricefetcher.integration.repository;

import com.devinho.pricefetcher.model.SupportedEcommerce;
import com.devinho.pricefetcher.model.dto.Currency;
import com.devinho.pricefetcher.model.entity.EmailAlert;
import com.devinho.pricefetcher.model.entity.ScrapedProductRecord;
import com.devinho.pricefetcher.repository.EmailAlertRepository;
import com.devinho.pricefetcher.repository.ScrapedProductRecordRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
public class EmailAlertRepositoryTest {

    @Autowired
    private EmailAlertRepository emailAlertRepository;
    @Autowired
    private ScrapedProductRecordRepository scrapedProductRecordRepository;

    private static EmailAlert emailAlert;

    private static final List<ScrapedProductRecord> scrapedProductRecords = new ArrayList<>();

    @BeforeAll
    @SneakyThrows
    static void setupTestEntites() {
        emailAlert = new EmailAlert(
                "Test alert name",
                "Test alert description",
                LocalDateTime.now(),
                "test@gg.com",
                new URL("https://amazon.com/"),
                SupportedEcommerce.AMAZON);
        scrapedProductRecords.add(new ScrapedProductRecord(1000.00, Currency.PLN, LocalDateTime.now(), emailAlert));
        scrapedProductRecords.add(new ScrapedProductRecord(300.00, Currency.PLN, LocalDateTime.now(), emailAlert));
        scrapedProductRecords.add(new ScrapedProductRecord(500.00, Currency.PLN, LocalDateTime.now(), emailAlert));
    }

    @Test
    @SneakyThrows
    void should_DeleteAllScrapedProductRecords_When_EmailAlertIsDeleted() {
        //GIVEN
        scrapedProductRecords.forEach(record -> emailAlert.addScrapedProductRecord(record));
        EmailAlert savedEmailAlert = emailAlertRepository.save(emailAlert);
        //WHEN
        List<ScrapedProductRecord> beforeDelete = scrapedProductRecordRepository.findAllByEmailAlert(emailAlert);
        emailAlertRepository.deleteById(savedEmailAlert.getAlertId());
        List<ScrapedProductRecord> afterDelete = scrapedProductRecordRepository.findAllByEmailAlert(emailAlert);
        //THEN
        Assertions.assertEquals(scrapedProductRecords.size(), beforeDelete.size());
        Assertions.assertTrue(afterDelete.isEmpty());
    }
}
