package com.cryptocurrency.rates.commons.restClient.mapper;

import com.cryptocurrency.rates.commons.restClient.vo.CurrencyApiResponse;
import com.cryptocurrency.rates.rest.currencyEnum.CurrencyApiExt;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CurrencyApiResponseMapper {
    private final ObjectMapper objectMapper;

    public CurrencyApiResponseMapper() {
        this.objectMapper = new ObjectMapper();
    }

    public CurrencyApiResponse mapResponse(String jsonResponse, CurrencyApiExt currencyApiExt) {
        switch (currencyApiExt) {
            case JSDEDLIVR:
                return mapCurrencyFreaksResponse(jsonResponse);
            case OTHERAPI:
                return mapOtherApiResponse(jsonResponse);
            default:
                throw new IllegalArgumentException("Unknown API name: " + currencyApiExt.name());
        }
    }

    private Map<String, Object> mapRawResponse(String jsonResponse) {
        try {
            return objectMapper.readValue(jsonResponse, new TypeReference<>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse API response dynamically", e);
        }
    }

    private CurrencyApiResponse mapCurrencyFreaksResponse(String jsonResponse) {
        Map<String, Object> rawResponse = mapRawResponse(jsonResponse);
        return mapToCryptocurrencyDetailsResponse(rawResponse);
    }

    private CurrencyApiResponse mapToCryptocurrencyDetailsResponse(Map<String, Object> rawResponse) {
        Map.Entry<String, Map<String, Object>> ratesEntry = rawResponse.entrySet()
                .stream()
                .filter(entry -> entry.getValue() instanceof Map)
                .map(entry -> Map.entry(entry.getKey(), (Map<String, Object>) entry.getValue()))
                .findFirst()
                .orElse(null);

        String source = null;
        Map<String, Double> rates = new HashMap<>();

        if (ratesEntry != null) {
            source = ratesEntry.getKey();
            rates = ratesEntry.getValue().entrySet()
                    .stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> {
                                try {
                                    return Double.valueOf(entry.getValue().toString());
                                } catch (NumberFormatException e) {
                                    return 0.0;
                                }
                            }
                    ));
        }

        return CurrencyApiResponse.builder()
                .source(source)
                .rates(rates)
                .errorMessages(null)
                .build();
    }


    private CurrencyApiResponse mapOtherApiResponse(String jsonResponse) {
        throw new UnsupportedOperationException("Method not implemented yet");
    }
}
