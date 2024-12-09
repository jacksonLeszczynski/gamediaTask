package com.cryptocurrency.rates.commons.restClient.vo;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public class FilterParameter {
    private final List<String> values;

    public FilterParameter(String singleValue) {
        this.values = Collections.singletonList(singleValue);
    }

    public FilterParameter(List<String> multipleValues) {
        this.values = multipleValues;
    }

    @Override
    public String toString() {
        return String.join(",", values);
    }
}
