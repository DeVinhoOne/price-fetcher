package com.devinho.pricefetcher.repository;

import com.devinho.pricefetcher.model.entity.EmailAlert;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface EmailAlertRepository extends CrudRepository<EmailAlert, UUID> {

    @Query("""
            SELECT new com.devinho.pricefetcher.model.dto.alert.EmailAlertDto(
            e.alertId, e.alertName, e.email, e.productUrl, e.ecommerce) 
            FROM EmailAlert e
            """)
    List<com.devinho.pricefetcher.model.dto.alert.EmailAlertDto> findAllDto();
}