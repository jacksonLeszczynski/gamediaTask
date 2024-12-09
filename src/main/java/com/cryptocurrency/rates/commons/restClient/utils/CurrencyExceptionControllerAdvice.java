package com.cryptocurrency.rates.commons.restClient.utils;

import com.cryptocurrency.rates.commons.restClient.vo.CurrencyApiResponse;
import com.cryptocurrency.rates.commons.restClient.vo.CurrencyException;
import com.cryptocurrency.rates.commons.restClient.vo.ResponseCodeStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class CurrencyExceptionControllerAdvice {

    @ExceptionHandler({CurrencyException.class})
    public CurrencyApiResponse handleCurrencyException(final CurrencyException ex) {
        log.warn("Exception with app was thrown: {}", ex.getMessage());

        CurrencyApiResponse response = CurrencyApiResponse.builder()
                .responseStatus(ResponseCodeStatus.APP_ERROR)
                .errorMessages(List.of(ex.getMessage()))
                .build();

        return response;
    }

    @ExceptionHandler({Exception.class})
    public CurrencyApiResponse handleException(final Exception ex) {
        log.warn("Exception with http was thrown: {}", ex.getMessage());

        CurrencyApiResponse response = CurrencyApiResponse.builder()
                .responseStatus(ResponseCodeStatus.HTTP_ERROR)
                .errorMessages(List.of(ex.getMessage()))
                .build();

        return response;
    }
}
