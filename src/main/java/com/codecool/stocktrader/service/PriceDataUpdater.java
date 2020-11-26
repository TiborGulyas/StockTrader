package com.codecool.stocktrader.service;

import com.codecool.stocktrader.component.ApiCall;
import com.codecool.stocktrader.model.LastPrice;
import com.codecool.stocktrader.model.Stock;
import com.codecool.stocktrader.repository.StockRepository;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class PriceDataUpdater {
    private final String resolution1min = "1";
    private final String resolution5min = "5";
    private final String resolution1day = "D";

    @Autowired
    StockRepository stockRepository;

    @Autowired
    private ApiCall apiCall;

    @Autowired
    private ApiStringProvider apiStringProvider;

    @Autowired
    private LastPricePersister lastPricePersister;

    @Autowired
    CandlePersister candlePersister;

    @Autowired
    OfferScanner offerScanner;

    @Scheduled(fixedDelay = 30000)
    public void updateLastPrices() throws IOException {
        System.out.println("lastPrice updater running");
        List<Stock> stocks = stockRepository.findAll();
        for (Stock stock: stocks) {
            String symbol = stock.getSymbol();
            String currentPricePath = apiStringProvider.provideApiStringForQuote(symbol);
            System.out.println("currentPricePath: "+currentPricePath);
            JsonObject response = apiCall.getResult(currentPricePath);
            System.out.println("getquote: "+response);
            lastPricePersister.persistCurrentPrice(response, symbol);
        }
        offerScanner.matchUserOffers();
    }

    @Scheduled(fixedDelay = 300000)
    public void updateCandlesMin5() throws IOException {
        System.out.println("candle min5 updater running");
        List<Stock> stocks = stockRepository.findAll();
        for (Stock stock : stocks) {
            String symbol = stock.getSymbol();
            String currentPricePathMIN5 = apiStringProvider.provideApiStringForCandle(symbol, resolution5min);
            System.out.println("currentPricePath: " + currentPricePathMIN5);
            JsonObject response = apiCall.getResult(currentPricePathMIN5);
            System.out.println("getcandle: " + response);
            candlePersister.persistCandle(response, symbol, resolution5min);

        }
    }

    @Scheduled(fixedDelay = 86400000)
    public void updateCandlesDay() throws IOException {
        System.out.println("candle day updater running");
        List<Stock> stocks = stockRepository.findAll();
        for (Stock stock : stocks) {
            String symbol = stock.getSymbol();
            String currentPricePathMIN5 = apiStringProvider.provideApiStringForCandle(symbol, resolution1day);
            System.out.println("currentPricePath: " + currentPricePathMIN5);
            JsonObject response = apiCall.getResult(currentPricePathMIN5);
            System.out.println("getcandle: " + response);
            candlePersister.persistCandle(response, symbol, resolution1day);

        }
    }
}
