package com.devinho.pricefetcher.service;

import com.devinho.pricefetcher.model.dto.Currency;
import com.devinho.pricefetcher.model.dto.Price;
import com.devinho.pricefetcher.model.dto.Product;
import com.devinho.pricefetcher.model.dto.Products;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
class AmazonFetcher {

    @Value("${amazon-config.session-id}")
    private String sessionId;

    public Products fetchProductsPricing(List<String> amazonUrls) {
        log.info("Fetching Amazon prices for {} products", amazonUrls.size());
        List<Product> products = new ArrayList<>();
        for (String url : amazonUrls) {
            var document = getMainDocument(url);
            var brand = getBrand(document);
            var price = getPrice(document);
            products.add(new Product(brand, price, url));
        }
        return new Products(products);
    }

    private Document getMainDocument(String url) {
        try {
            return Jsoup.connect(url)
                    .cookie("session-id", sessionId)
                    .get();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private String getBrand(Document document) {
        var brandElements = document.getElementsByClass("a-spacing-small po-brand");
        if (brandElements.isEmpty()) {
            System.err.println("Unable to establish BRAND NAME for given product");
            return "";
        } else {
            return brandElements.get(0)
                    .getElementsByTag("td")
                    .get(1)
                    .getElementsByTag("span")
                    .text();
        }
    }

    private Price getPrice(Document document) {
        var elements = document.select("#corePrice_feature_div > div > span > span.a-offscreen");
        if (elements.isEmpty()) {
            System.err.println("Unable to establish PRICE for given product");
            return new Price(-1d, Currency.UNKNOWN);
        }
        var dirtyPrice = elements.get(0).childNodes().get(0).toString();
        return parsePrice(dirtyPrice);
    }

    private Price parsePrice(String dirtyPrice) {
        if (dirtyPrice == null || dirtyPrice.isEmpty())
            throw new IllegalArgumentException("Given price is empty or null.");
        var a = dirtyPrice.replaceAll("&nbsp;", "")
                .replaceAll("zł", "")
                .replaceAll("€", "")
                .replaceAll(",", ".");
        return new Price(Double.parseDouble(a), getCurrency(dirtyPrice));
    }

    private Currency getCurrency(String dirtyPrice) {
        Currency[] currencies = Currency.values();
        for (Currency currency : currencies) {
            if (dirtyPrice.contains(currency.value)) return currency;
        }
        return Currency.UNKNOWN;
    }
}
