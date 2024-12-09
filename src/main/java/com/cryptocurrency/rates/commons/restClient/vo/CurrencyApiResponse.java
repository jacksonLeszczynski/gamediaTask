package com.cryptocurrency.rates.commons.restClient.vo;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CurrencyApiResponse implements Response {
    protected String source;
    protected Map<String, Double> rates;
    private String from;

    @JsonIgnore
    private Map<String, CurrencyApiResponse.CurrencyConversionDetail> convertedData;

    @JsonAnyGetter
    public Map<String, CurrencyApiResponse.CurrencyConversionDetail> getConvertedCurrenciesAsFields() {
        return convertedData;
    }
    protected ResponseCodeStatus responseStatus;
    protected List<String> errorMessages;

    public CurrencyApiResponse getFilterData(FilterParameter filterParameter) {
        if (filterParameter == null) {
            return this;
        }
        Map<String, Double> filteredRates = this.rates.entrySet()
                .stream()
                .filter(entry -> filterParameter.getValues().contains(entry.getKey().toUpperCase()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return CurrencyApiResponse.builder()
                .source(this.source)
                .rates(filteredRates)
                .responseStatus(this.responseStatus)
                .errorMessages(this.errorMessages)
                .build();
    }

    @Getter
    @Builder
    public static class CurrencyConversionDetail {
        private double rate;
        private double amount;
        private double result;
        private double fee;
    }
}
