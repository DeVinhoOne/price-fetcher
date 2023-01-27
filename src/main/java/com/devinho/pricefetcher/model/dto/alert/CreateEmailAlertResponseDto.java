package com.devinho.pricefetcher.model.dto.alert;

import java.time.LocalDateTime;

public record CreateEmailAlertResponseDto(String alertName, LocalDateTime alertCreatedAt) {
}
