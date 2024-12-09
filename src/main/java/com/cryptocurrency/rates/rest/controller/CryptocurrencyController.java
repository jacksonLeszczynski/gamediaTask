package com.cryptocurrency.rates.rest.controller;

import com.cryptocurrency.rates.commons.restClient.vo.CurrencyApiResponse;
import com.cryptocurrency.rates.commons.restClient.vo.FilterParameter;
import com.cryptocurrency.rates.rest.dto.CryptocurrencyConversionRequest;
import com.cryptocurrency.rates.rest.service.CryptoCurrencyRestClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/currencies")
public class CryptocurrencyController {
    private final CryptoCurrencyRestClientService cryptoCurrencyRestClientService;

    @Autowired
    public CryptocurrencyController(CryptoCurrencyRestClientService cryptoCurrencyRestClientService) {
        this.cryptoCurrencyRestClientService = cryptoCurrencyRestClientService;
    }

    @GetMapping("/{currency}")
    public ResponseEntity<CurrencyApiResponse> cryptoCurrency(
            @PathVariable String currency,
            @RequestParam(value = "filter", required = false) FilterParameter filterParam) {

        CurrencyApiResponse response = cryptoCurrencyRestClientService.getDataFromApi(currency.toLowerCase(), filterParam);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/exchange")
    public ResponseEntity<CurrencyApiResponse> cryptoCurrencyCalculateAmount(@RequestBody @Valid CryptocurrencyConversionRequest request) {
        CurrencyApiResponse response = cryptoCurrencyRestClientService.convertCryptocurrencyData(request);

        return ResponseEntity.ok(response);
    }
}
