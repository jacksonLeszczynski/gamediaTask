package com.cryptocurrency.rates.rest.service;

import com.cryptocurrency.rates.commons.restClient.utils.AbstractExternalApiService;
import com.cryptocurrency.rates.commons.restClient.vo.CurrencyApiResponse;
import com.cryptocurrency.rates.commons.restClient.vo.CurrencyException;
import com.cryptocurrency.rates.commons.restClient.vo.FilterParameter;
import com.cryptocurrency.rates.rest.config.CryptoCurrencyRestClientConfig;
import com.cryptocurrency.rates.rest.currencyEnum.CurrencyApiExt;
import com.cryptocurrency.rates.rest.dto.CryptocurrencyConversionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class CryptoCurrencyRestClientService extends AbstractExternalApiService<CurrencyApiResponse> {

    public static final double FEE_PERCENT = 0.01;

    @Autowired
    public CryptoCurrencyRestClientService(CryptoCurrencyRestClientConfig cryptoCurrencyRestClientConfig) {
        super(cryptoCurrencyRestClientConfig);
    }

    @Override
    public CurrencyApiResponse parseResponse(HttpResponse<String> response, FilterParameter filterParam) {
        try {
            return objectMapper.mapResponse(response.body(), CurrencyApiExt.fromCurrencyName(config.getApiName()))
                    .getFilterData(filterParam);
        } catch (RuntimeException e) {
            throw new CurrencyException("Failed to parse API response");
        }
    }

    public CurrencyApiResponse convertCryptocurrencyData(CryptocurrencyConversionRequest request) {
        Map<String, CurrencyApiResponse.CurrencyConversionDetail> conversionDetails = new HashMap<>();
        ExecutorService executor = createCurrencyConversionDetail(request, conversionDetails);

        CurrencyApiResponse response = CurrencyApiResponse.builder()
                .from(request.getFrom())
                .convertedData(conversionDetails)
                .build();

        executor.shutdown();

        return response;
    }

    ExecutorService createCurrencyConversionDetail(CryptocurrencyConversionRequest request, Map<String, CurrencyApiResponse.CurrencyConversionDetail> conversionDetails) {
        ExecutorService executor = Executors.newFixedThreadPool(config.getConnectTimeout());

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (String targetCurrency : request.getTo()) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    double rate = getConversionRate(request.getFrom(), targetCurrency);
                    double result = rate * request.getAmount();
                    double fee = result * FEE_PERCENT;

                    CurrencyApiResponse.CurrencyConversionDetail detail = CurrencyApiResponse.CurrencyConversionDetail.builder()
                            .rate(rate)
                            .amount(request.getAmount())
                            .result(result)
                            .fee(fee)
                            .build();

                    synchronized (conversionDetails) {
                        conversionDetails.put(targetCurrency, detail);
                    }
            }, executor);

            future.exceptionally(ex -> {
                throw new RuntimeException("Error processing conversion post request", ex);
            });

            futures.add(future);
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        return executor;
    }

    private double getConversionRate(String fromCurrency, String toCurrency) {
        var response = getDataFromApi(fromCurrency, new FilterParameter(toCurrency));

        return Optional.ofNullable(response.getRates())
                .map(rates -> rates.get(config.getFormatCurrency(toCurrency)))
                .map(Number::doubleValue)
                .orElseThrow(() -> new IllegalStateException("Rates data is missing or invalid."));
    }

}
