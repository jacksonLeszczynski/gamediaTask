package com.cryptocurrency.rates.rest.currencyEnum;

public enum CurrencyApiExt {
    JSDEDLIVR("jsdelivr"),
    OTHERAPI("otherapi");

    String currencyName;

    public String getCurrencyName() {
        return currencyName;
    }

    CurrencyApiExt(String currencyName) {
        this.currencyName = currencyName;
    }

    public static CurrencyApiExt fromCurrencyName(String currencyName) {
        for (CurrencyApiExt apiExt : values()) {
            if (apiExt.getCurrencyName().equalsIgnoreCase(currencyName)) {
                return apiExt;
            }
        }
        throw new IllegalArgumentException("No enum constant with currencyName: " + currencyName);
    }
}
