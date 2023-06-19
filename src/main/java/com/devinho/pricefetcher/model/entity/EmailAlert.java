package com.devinho.pricefetcher.model.entity;

import com.devinho.pricefetcher.model.SupportedEcommerce;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class EmailAlert {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID alertId;

    @Column(nullable = false)
    private String alertName;

    private String alertDescription;

    @Column(nullable = false)
    private LocalDateTime alertCreatedAt;

    private LocalDateTime lastAlertSentAt;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private URL productUrl;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private SupportedEcommerce ecommerce;

    @OneToMany(mappedBy = "emailAlert", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScrapedProductRecord> scrapedProductRecords = new ArrayList<>();

    public EmailAlert(String alertName, String alertDescription, LocalDateTime alertCreatedAt, String email, URL productUrl, SupportedEcommerce ecommerce) {
        this.alertName = alertName;
        this.alertDescription = alertDescription;
        this.alertCreatedAt = alertCreatedAt;
        this.email = email;
        this.productUrl = productUrl;
        this.ecommerce = ecommerce;
    }

    public void addScrapedProductRecord(ScrapedProductRecord scrapedProductRecord) {
        scrapedProductRecord.setEmailAlert(this);
        scrapedProductRecords.add(scrapedProductRecord);
    }

    public void removeScrapedProductRecord(ScrapedProductRecord scrapedProductRecord) {
        scrapedProductRecord.setEmailAlert(null);
        scrapedProductRecords.remove(scrapedProductRecord);
    }
}
