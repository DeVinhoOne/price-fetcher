package com.devinho.pricefetcher.model.dto.alert;

import com.devinho.pricefetcher.model.SupportedEcommerce;

import java.net.URL;
import java.util.UUID;

public record EmailAlertDto(UUID alertId, String alertName, String email, URL productUrl,
                            SupportedEcommerce ecommerce) {
}
