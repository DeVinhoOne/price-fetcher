package com.devinho.pricefetcher.controller;

import com.devinho.pricefetcher.model.dto.ProductUrls;
import com.devinho.pricefetcher.model.dto.Products;
import com.devinho.pricefetcher.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    @PostMapping("/amazon/price")
    public Products getAmazonProductsPricing(@RequestBody ProductUrls productUrls) {
        return productService.getAmazonProductsPricing(productUrls);
    }

}
