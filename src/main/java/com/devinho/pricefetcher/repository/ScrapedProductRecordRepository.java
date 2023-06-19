package com.devinho.pricefetcher.repository;

import com.devinho.pricefetcher.model.entity.EmailAlert;
import com.devinho.pricefetcher.model.entity.ScrapedProductRecord;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ScrapedProductRecordRepository extends CrudRepository<ScrapedProductRecord, UUID> {

    @Query("""
        SELECT r FROM ScrapedProductRecord r
        WHERE r.emailAlert.alertId = ?1
        ORDER BY r.createdAt DESC
        LIMIT 1
    """)
    Optional<ScrapedProductRecord> findLatestByEmailAlertId(UUID emailAlertId);

    List<ScrapedProductRecord> findAllByEmailAlert(EmailAlert emailAlert);
}