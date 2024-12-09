package com.cryptocurrency.rates.rest.config;

import com.cryptocurrency.rates.commons.restClient.utils.ExternalApiConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "api.cryptocurrency-ext")
public class CryptoCurrencyRestClientConfig implements ExternalApiConfig {

    private String baseUrl;
    private int connectTimeout;
    private int readTimeout;
    private String apiName;
    private String suffix;
    private String currencyThreadNums;
    private int apiThreadNums;

    @Override
    public String getFormatCurrency(String currency) {
        return currency != null ? currency.trim().toLowerCase() : null;
    }
}
