package com.cryptocurrency.rates.rest.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.List;

@Data
public class CryptocurrencyConversionRequest {
    @NotNull(message = "The 'from' field is required")
    private String from;
    @NotEmpty(message = "The 'to' field must contain at least one currency")
    private List<String> to;
    @Positive(message = "The 'amount' field must be greater than 0")
    private double amount;
}