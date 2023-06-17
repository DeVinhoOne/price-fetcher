package com.devinho.pricefetcher.mapper;

import com.devinho.pricefetcher.model.dto.ScrapedProductDto;
import com.devinho.pricefetcher.model.entity.ScrapedProductRecord;

public class ScrapedProductRecordMapper {

    public static ScrapedProductRecord mapScrapedProductDtoToScrapedProductRecord(ScrapedProductDto dto) {
        var newEntity = new ScrapedProductRecord();
        newEntity.setLastPrice(dto.price().value());
        newEntity.setCurrency(dto.price().currency());
        return newEntity;
    }

}
