package com.devinho.pricefetcher.service;

import com.devinho.pricefetcher.model.dto.ProductUrls;
import com.devinho.pricefetcher.model.dto.Products;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final AmazonFetcher amazonFetcher;

    public Products getAmazonProductsPricing(ProductUrls productUrls) {
        switch (productUrls.getEcommerce()) {
            case AMAZON:
                return amazonFetcher.fetchProductsPricing(productUrls.getUrls());
        }
        return new Products(Collections.emptyList());
    }
}