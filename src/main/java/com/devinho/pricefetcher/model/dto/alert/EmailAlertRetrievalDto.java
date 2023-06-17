package com.devinho.pricefetcher.model.dto.alert;

import com.devinho.pricefetcher.model.SupportedEcommerce;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.UUID;

public record EmailAlertRetrievalDto(UUID alertId, String alertName, String alertDescription,
                                     LocalDateTime alertCreatedAt, LocalDateTime lastAlertSentAt,
                                     String email, URL productUrl, SupportedEcommerce ecommerce) {
}
