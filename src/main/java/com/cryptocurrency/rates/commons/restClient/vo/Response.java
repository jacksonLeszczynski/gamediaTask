package com.cryptocurrency.rates.commons.restClient.vo;

import java.util.List;

public interface Response {
    ResponseCodeStatus getResponseStatus();
    List<String> getErrorMessages();
}
