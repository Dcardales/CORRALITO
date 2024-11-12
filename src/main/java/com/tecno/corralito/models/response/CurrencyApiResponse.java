package com.tecno.corralito.models.response;

import java.math.BigDecimal;
import java.util.Map;

public class CurrencyApiResponse {

    private Map<String, BigDecimal> rates; // Mapa de las tasas de cambio

    public Map<String, BigDecimal> getRates() {
        return rates;
    }

    public void setRates(Map<String, BigDecimal> rates) {
        this.rates = rates;
    }
}
