package com.devinho.pricefetcher.mapper;

import com.devinho.pricefetcher.model.dto.Currency;
import com.devinho.pricefetcher.model.dto.Price;
import com.devinho.pricefetcher.model.dto.ScrapedProductDto;
import com.devinho.pricefetcher.model.entity.ScrapedProductRecord;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScrapedProductRecordMapperTest {

    @Test
    void mapScrapedProductDtoToScrapedProductRecord() {
        ScrapedProductDto dto = new ScrapedProductDto(new Price(16d, Currency.PLN), "https://google.com/");
        ScrapedProductRecord entity = ScrapedProductRecordMapper.mapScrapedProductDtoToScrapedProductRecord(dto);
        assertEquals(dto.price().value(), entity.getLastPrice());
        assertEquals(dto.price().currency(), entity.getCurrency());
    }
}