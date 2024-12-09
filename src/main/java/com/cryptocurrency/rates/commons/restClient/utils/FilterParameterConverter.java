package com.cryptocurrency.rates.commons.restClient.utils;

import com.cryptocurrency.rates.commons.restClient.vo.FilterParameter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class FilterParameterConverter implements Converter<String, FilterParameter> {

    @Override
    public FilterParameter convert(String source) {
        if (source.contains(",")) {
            List<String> values = Arrays.stream(source.split(","))
                    .map(String::trim)
                    .toList();
            return new FilterParameter(values);
        }
        return new FilterParameter(source.trim());
    }
}

