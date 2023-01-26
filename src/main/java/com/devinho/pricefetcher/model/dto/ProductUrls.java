package com.devinho.pricefetcher.model.dto;

import com.devinho.pricefetcher.model.SupportedEcommerce;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductUrls {
    private SupportedEcommerce ecommerce;
    private List<String> urls;
}
