package com.cryptocurrency.rates.commons.restClient.utils;

public interface ExternalApiConfig {
    String getBaseUrl();
    String getSuffix();
    int getConnectTimeout();
    int getReadTimeout();
    String getApiName();
    String getFormatCurrency(String currency);
    int getApiThreadNums();
}
