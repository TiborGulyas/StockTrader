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
public class LastPriceUpdater {
    @Autowired
    StockRepository stockRepository;

    @Autowired
    private ApiCall apiCall;

    @Autowired
    private LastPricePersister lastPricePersister;

    String candleAPIToken = "&token="+System.getenv("FINNHUB_TOKEN");

    @Scheduled(fixedDelay = 30000)
    public void updateLastPrices() throws IOException {
        System.out.println("updater running");
        List<Stock> stocks = stockRepository.findAll();
        for (Stock stock: stocks) {
            String symbol = stock.getSymbol();
            String currentPricePath = ApiStringProvider.currentPriceAPIbase+symbol+candleAPIToken;
            System.out.println("currentPricePath: "+currentPricePath);
            JsonObject response = apiCall.getResult(currentPricePath);
            System.out.println("getquote: "+response);
            lastPricePersister.persistCurrentPrice(response, symbol);
        }
    }
}
