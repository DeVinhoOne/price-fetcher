package com.devinho.pricefetcher.exception.handler;

import com.devinho.pricefetcher.exception.model.GenericPriceFetcherException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class PriceFetcherExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GenericPriceFetcherException.class)
    protected ResponseEntity<ErrorData> handle(GenericPriceFetcherException ex) {
        var exceptionName = ex.getClass().getSimpleName();
        log.error("[{}] {}", exceptionName, ex.getMessage());
        return ResponseEntity
                .status(ex.getHttpStatusCode())
                .body(new ErrorData(exceptionName, ex.getMessage()));
    }

    record ErrorData(String error, String message) {}
}
