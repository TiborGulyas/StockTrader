package com.codecool.stocktrader.controller;

import com.codecool.stocktrader.component.ApiCall;
import com.codecool.stocktrader.model.CandleContainer;
import com.codecool.stocktrader.model.LastPrice;
import com.codecool.stocktrader.model.Stock;
import com.codecool.stocktrader.repository.StockRepository;
import com.codecool.stocktrader.service.CandlePersister;
import com.codecool.stocktrader.service.LastPricePersister;
import com.codecool.stocktrader.service.UTCTimeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.*;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/stock")
public class StockController {

    @Autowired
    private ApiCall apiCall;

    @Autowired
    private UTCTimeProvider utcTimeProvider;

    @Autowired
    private CandlePersister candlePersister;

    @Autowired
    private LastPricePersister lastPricePersister;

    @Autowired
    private StockRepository stockRepository;


    //private final String basicAPIEndpoint = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=IBM&interval=1min&outputsize=full&apikey=KSIT3ZSPQP1NLNY4";
    //private final String basicAPIEndpoint = "https://finnhub.io/api/v1/stock/candle?symbol=AAPL&resolution=1&from=1605543327&to=1605629727&token=buptifn48v6q16g20rpg";


    private final String candleAPIBase = "https://finnhub.io/api/v1/stock/candle?";
    private final String currentPriceAPIbase = "https://finnhub.io/api/v1/quote?symbol=";
    String candleAPIToken = "&token="+System.getenv("FINNHUB_TOKEN");

    @GetMapping("/getcandle/{symbol}/{resolution}")
    public CandleContainer returnIntraday(@PathVariable("symbol") String symbol, @PathVariable("resolution") String resolution) throws IOException {

        Map<String, Long> utcTimeStamps = utcTimeProvider.provideUTCTimeStamps(resolution);
        System.out.println(utcTimeStamps);
        String candleAPISymbol = "symbol="+symbol;
        String candleAPIResolution = "&resolution="+resolution;
        String candleAPIFrom = "&from="+utcTimeStamps.get("from");
        String candleAPITo = "&to="+utcTimeStamps.get("to");
        String candleAPIPath = candleAPIBase+candleAPISymbol+candleAPIResolution+candleAPIFrom+candleAPITo+candleAPIToken;
        JsonObject response = apiCall.getResult(candleAPIPath);
        System.out.println(response);
        CandleContainer candleReturn = candlePersister.persistCandle(response, symbol, resolution);
        return candleReturn;

    }

    @GetMapping("/getquote/{symbol}")
    public Stock returnCurrentPrice(@PathVariable("symbol") String symbol) throws IOException {
        System.out.println(symbol);
        String currentPricePath = currentPriceAPIbase+symbol+candleAPIToken;
        System.out.println("currentPricePath: "+currentPricePath);
        JsonObject response = apiCall.getResult(currentPricePath);
        System.out.println("getquote: "+response);
        Stock lastPrice = lastPricePersister.persistCurrentPrice(response, symbol);
        return lastPrice;
    }
}
