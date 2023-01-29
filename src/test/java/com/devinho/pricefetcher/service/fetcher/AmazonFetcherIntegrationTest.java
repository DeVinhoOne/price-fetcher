package com.devinho.pricefetcher.service.fetcher;

import com.devinho.pricefetcher.model.dto.Currency;
import com.devinho.pricefetcher.model.dto.ScrapedProductDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor
class AmazonFetcherIntegrationTest {

//    private final AmazonFetcher amazonFetcher;

    @Test
    void fetchProductPricing() throws MalformedURLException {
        var urlToFetch = new URL("https://www.amazon.pl/QuietComfort-bezprzewodowe-sluchawki-Bluetooth-mikrofonem/dp/B0BDT54SGD");
//        var scrapedProductDto = amazonFetcher.fetchProductPricing(urlToFetch);
//        assertNotEquals(-1d, scrapedProductDto.price().value());
//        assertNotEquals(Currency.UNKNOWN, scrapedProductDto.price().currency());
    }
}