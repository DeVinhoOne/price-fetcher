package com.devinho.pricefetcher.model.dto.alert;

import com.devinho.pricefetcher.model.SupportedEcommerce;

import java.net.URL;

public record CreateEmailAlertDto(String alertName,
                                  String alertDescription,
                                  String email,
                                  URL productUrl,
                                  SupportedEcommerce ecommerce) {
}
