package com.devinho.pricefetcher.repository;

import com.devinho.pricefetcher.model.dto.alert.EmailAlertDto;
import com.devinho.pricefetcher.model.dto.alert.EmailAlertRetrievalDto;
import com.devinho.pricefetcher.model.entity.EmailAlert;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface EmailAlertRepository extends CrudRepository<EmailAlert, UUID> {

    @Query("""
            SELECT new com.devinho.pricefetcher.model.dto.alert.EmailAlertDto(
            e.alertId, e.alertName, e.email, e.productUrl, e.ecommerce) 
            FROM EmailAlert e
            """)
    List<EmailAlertDto> findAllDto();

    @Modifying
    @Query("""
            UPDATE EmailAlert e
            SET e.lastAlertSentAt = ?2
            WHERE e.alertId = ?1
            """)
    void updateLastAlertSentAtById(UUID emailAlertId, LocalDateTime lastAlertSentAt);

    List<EmailAlertRetrievalDto> getEmailAlertsByEmail(String email);
}