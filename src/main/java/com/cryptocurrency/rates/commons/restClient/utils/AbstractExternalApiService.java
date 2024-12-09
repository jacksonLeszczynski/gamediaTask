package com.cryptocurrency.rates.commons.restClient.utils;

import com.cryptocurrency.rates.commons.restClient.mapper.CurrencyApiResponseMapper;
import com.cryptocurrency.rates.commons.restClient.vo.CurrencyException;
import com.cryptocurrency.rates.commons.restClient.vo.FilterParameter;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public abstract class AbstractExternalApiService<T> implements RestClientService {
    protected final ExternalApiConfig config;
    protected final HttpClient httpClient;
    protected final CurrencyApiResponseMapper objectMapper;

    protected AbstractExternalApiService(ExternalApiConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("ExternalApiConfig cannot be null");
        }
        this.config = config;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(config.getConnectTimeout()))
                .build();
        this.objectMapper = new CurrencyApiResponseMapper();
    }

    public abstract T parseResponse(HttpResponse<String> response, FilterParameter filterParam);

    @Override
    public T getDataFromApi(String endpoint, FilterParameter filterParam) {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(config.getBaseUrl() + config.getFormatCurrency(endpoint) + config.getSuffix()))
                        .timeout(Duration.ofMillis(config.getReadTimeout()))
                        .GET()
                        .header("Accept", "application/json")
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    return parseResponse(response, filterParam);
                } else {
                    throw new CurrencyException("App Error: " + response.statusCode());
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to fetch data from API", e);
            }
    }

}
