package com.codecool.stocktrader.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
public class ApiStringProvider {
    private final String candleAPIBase = "https://finnhub.io/api/v1/stock/candle?";
    private final String currentPriceAPIbase = "https://finnhub.io/api/v1/quote?symbol=";
    private String candleAPIToken = "&token=" + System.getenv("FINNHUB_TOKEN");

    @Autowired
    private UTCTimeProvider utcTimeProvider;

    public String provideApiStringForCandle(String symbol, String resolution) {
        Map<String, Long> utcTimeStamps = utcTimeProvider.provideUTCTimeStamps(resolution);
        System.out.println(utcTimeStamps);
        String candleAPISymbol = "symbol=" + symbol;
        String candleAPIResolution = "&resolution=" + resolution;
        String candleAPIFrom = "&from=" + utcTimeStamps.get("from");
        String candleAPITo = "&to=" + utcTimeStamps.get("to");
        return candleAPIBase + candleAPISymbol + candleAPIResolution + candleAPIFrom + candleAPITo + candleAPIToken;
    }

    public String provideApiStringForQuote(String symbol){
        return currentPriceAPIbase + symbol + candleAPIToken;
    }
}
