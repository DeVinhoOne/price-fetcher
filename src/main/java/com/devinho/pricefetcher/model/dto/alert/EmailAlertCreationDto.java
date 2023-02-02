package com.devinho.pricefetcher.model.dto.alert;

import com.devinho.pricefetcher.model.SupportedEcommerce;

import java.net.URL;

public record EmailAlertCreationDto(String alertName,
                                    String alertDescription,
                                    String email,
                                    URL productUrl,
                                    SupportedEcommerce ecommerce) {
}
