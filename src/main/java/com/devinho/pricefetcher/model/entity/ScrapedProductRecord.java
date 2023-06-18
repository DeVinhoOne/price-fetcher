package com.devinho.pricefetcher.model.entity;

import com.devinho.pricefetcher.model.dto.Currency;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
public class ScrapedProductRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID recordId;

    @Column(nullable = false)
    private Double lastPrice;

    @Enumerated(EnumType.STRING)
    @Column(length = 5, nullable = false)
    private Currency currency;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email_alert_id")
    private EmailAlert emailAlert;
}
