package com.devinho.pricefetcher.exception.model;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class GenericPriceFetcherException extends RuntimeException {

    final HttpStatusCode httpStatusCode;

    public GenericPriceFetcherException(HttpStatusCode httpStatusCode, String message) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }
}
