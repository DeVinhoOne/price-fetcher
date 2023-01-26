package com.devinho.pricefetcher.service;

import com.devinho.pricefetcher.model.dto.ProductUrls;
import com.devinho.pricefetcher.model.dto.Products;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final AmazonFetcher amazonFetcher;

    public Products getAmazonProductsPricing(ProductUrls productUrls) {
        return amazonFetcher.fetchProductsPricing(productUrls.getUrls());
    }
}