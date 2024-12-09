package com.cryptocurrency.rates.commons.restClient.utils;

import com.cryptocurrency.rates.commons.restClient.vo.FilterParameter;

public interface RestClientService {
    <T> T getDataFromApi(String endpoint, FilterParameter filterParam);
}
