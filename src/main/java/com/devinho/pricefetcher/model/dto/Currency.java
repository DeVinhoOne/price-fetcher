package com.devinho.pricefetcher.model.dto;

public enum Currency {
    PLN("zł"),
    EUR("€"),
    UNKNOWN("unknown");

    public final String value;

    Currency(String value) {
        this.value = value;
    }
}
