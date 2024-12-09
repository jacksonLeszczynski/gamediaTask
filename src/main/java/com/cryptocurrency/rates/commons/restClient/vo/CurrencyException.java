package com.cryptocurrency.rates.commons.restClient.vo;

public class CurrencyException extends RuntimeException {
    ResponseCodeStatus responseCodeStatus;

    public CurrencyException(String message) {
        super(message);
    }
}
