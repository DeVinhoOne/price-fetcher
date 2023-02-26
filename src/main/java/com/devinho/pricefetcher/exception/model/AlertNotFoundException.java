package com.devinho.pricefetcher.exception.model;

import org.springframework.http.HttpStatusCode;

public class AlertNotFoundException extends GenericPriceFetcherException {
    public AlertNotFoundException(HttpStatusCode httpStatusCode, String message) {
        super(httpStatusCode, message);
    }
}
