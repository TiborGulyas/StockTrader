package com.codecool.stocktrader.service;

import com.codecool.stocktrader.model.LastPrice;
import com.codecool.stocktrader.model.Stock;
import com.codecool.stocktrader.repository.LastPriceRepository;
import com.codecool.stocktrader.repository.StockRepository;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Calendar;


@Component
public class LastPricePersister {

    @Autowired
    StockRepository stockRepository;

    @Autowired
    LastPriceRepository lastPriceRepository;

    public void persistCurrentPrice(JsonObject response, String symbol){
        float currentPrice = response.getAsJsonPrimitive("c").getAsFloat();
        float openPrice = response.getAsJsonPrimitive("o").getAsFloat();
        float highPrice = response.getAsJsonPrimitive("h").getAsFloat();
        float lowPrice = response.getAsJsonPrimitive("l").getAsFloat();
        float previousPrice = response.getAsJsonPrimitive("pc").getAsFloat();
        Calendar today = Calendar.getInstance();
        Stock stock = stockRepository.findBySymbol(symbol);
        LastPrice lastPriceObj = LastPrice.builder()
                .currentPrice(currentPrice)
                .highPrice(highPrice)
                .lowPrice(lowPrice)
                .openPrice(openPrice)
                .previousClosePrice(previousPrice)
                .timeOfRetrieval(today.getTime())
                .build();
        long originalLastPriceId;
        if (stock.getLastPrice() != null) {
            originalLastPriceId = stock.getLastPrice().getId();
            stock.setLastPrice(lastPriceObj);
            stockRepository.save(stock);
            lastPriceRepository.deleteById(originalLastPriceId);
            } else {
            stock.setLastPrice(lastPriceObj);
            stockRepository.save(stock);
        }
    }
}
