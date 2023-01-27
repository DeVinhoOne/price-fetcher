package com.devinho.pricefetcher.service;

import com.devinho.pricefetcher.model.dto.Currency;
import com.devinho.pricefetcher.model.dto.Price;
import com.devinho.pricefetcher.model.dto.ScrapedProductDto;
import com.devinho.pricefetcher.model.dto.Products;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
class AmazonFetcher {

    @Value("${amazon-config.session-id}")
    private String sessionId;

    private String sessionToken = "0UNHFzoptiY1I1P+4j2DH9QisLhuSUjme5Mpwd6GYv2fBE7pEqDxefNl+8KHgaleAbAwtXvK/kKKIHcVZh+3fNeas7rP4r3Hmiu7g1vF1qAB1nrIAiJuptUUP36/zGnHXris4/XkXNXvm70p32WBxBiCEDbS4GClOqQ6PpdrkhC3BIi/NpKLzeBt+iR44JtFW86ZkW80kUHlDZt8RAbT2suwo9e7Mj2zDS333tMCh5tIc1Csn2YUZg==";

    public Products fetchProductsPricing(List<String> amazonUrls) {
        log.info("Fetching Amazon prices for {} products", amazonUrls.size());
        List<ScrapedProductDto> scrapedProductDtos = new ArrayList<>();
        for (String url : amazonUrls) {
            var document = getMainDocument(url);
            var price = getPrice(document);
            scrapedProductDtos.add(new ScrapedProductDto(price, url));
        }
        return new Products(scrapedProductDtos);
    }

    public ScrapedProductDto fetchProductPricing(URL url) {
        var urlStr = url.toString();
        log.info("Fetching Amazon price [session-id: {}, url: {}]", sessionId, urlStr);
        var document = getMainDocument(urlStr);
        var price = getPrice(document);
        return new ScrapedProductDto(price, urlStr);
    }

    private Document getMainDocument(String url) {
        try {
            return Jsoup.connect(url)
                    .cookie("session-id", sessionId)
                    .cookie("session-token", sessionToken)
                    .get();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
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
