package com.devinho.pricefetcher.repository;

import com.devinho.pricefetcher.model.entity.ScrapedProductRecord;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ScrapedProductRecordRepository extends CrudRepository<ScrapedProductRecord, UUID> {
}