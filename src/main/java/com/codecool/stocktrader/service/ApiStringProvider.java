package com.codecool.stocktrader.service;

import org.springframework.stereotype.Component;

@Component
public class ApiStringProvider {
    public static final String candleAPIBase = "https://finnhub.io/api/v1/stock/candle?";
    public static final String currentPriceAPIbase = "https://finnhub.io/api/v1/quote?symbol=";
}
