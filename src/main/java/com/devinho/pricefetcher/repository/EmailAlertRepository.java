package com.devinho.pricefetcher.repository;

import com.devinho.pricefetcher.model.entity.EmailAlert;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface EmailAlertRepository extends CrudRepository<EmailAlert, UUID> {
}