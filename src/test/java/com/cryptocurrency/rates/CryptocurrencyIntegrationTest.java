package com.cryptocurrency.rates;


import com.cryptocurrency.rates.commons.restClient.vo.CurrencyApiResponse;
import com.cryptocurrency.rates.commons.restClient.vo.FilterParameter;
import com.cryptocurrency.rates.rest.controller.CryptocurrencyController;
import com.cryptocurrency.rates.rest.dto.CryptocurrencyConversionRequest;
import com.cryptocurrency.rates.rest.service.CryptoCurrencyRestClientService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CryptocurrencyController.class)
public class CryptocurrencyIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CryptoCurrencyRestClientService cryptoCurrencyRestClientService;

    @Test
    void testGetCryptoCurrency() throws Exception {
        //given
        CurrencyApiResponse mockResponse = Mockito.mock(CurrencyApiResponse.class);
        Mockito.when(cryptoCurrencyRestClientService.getDataFromApi(eq("btc"), any(FilterParameter.class)))
                .thenReturn(mockResponse);

        //when
        mockMvc.perform(get("/currencies/btc")
                        .param("filter", "USD,ETH")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        //then
        Mockito.verify(cryptoCurrencyRestClientService, Mockito.times(1))
                .getDataFromApi(eq("btc"), any(FilterParameter.class));
    }

    @Test
    void testPostCryptoCurrencyCalculateAmount() throws Exception {
        //given
        CurrencyApiResponse mockResponse = Mockito.mock(CurrencyApiResponse.class);
        Mockito.when(cryptoCurrencyRestClientService.convertCryptocurrencyData(any(CryptocurrencyConversionRequest.class)))
                .thenReturn(mockResponse);

        //when
        String requestBody = """
                {
                  "from": "BTC",
                  "to": ["ETH", "USDT"],
                  "amount": 121
                }
                """;


        mockMvc.perform(post("/currencies/exchange")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        //then
        Mockito.verify(cryptoCurrencyRestClientService, Mockito.times(1))
                .convertCryptocurrencyData(any(CryptocurrencyConversionRequest.class));
    }
}
