package com.codecool.stocktrader.service;

import com.codecool.stocktrader.model.LastPrice;
import com.codecool.stocktrader.model.Stock;
import com.codecool.stocktrader.repository.LastPriceRepository;
import com.codecool.stocktrader.repository.StockRepository;
import com.google.gson.JsonObject;
import org.apache.commons.math3.util.Precision;
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
        float currentPrice = NumberRounder.roundFloat(response.getAsJsonPrimitive("c").getAsFloat(), 2);
        float openPrice =  NumberRounder.roundFloat(response.getAsJsonPrimitive("o").getAsFloat(),2);
        float highPrice =  NumberRounder.roundFloat(response.getAsJsonPrimitive("h").getAsFloat(),2);
        float lowPrice =  NumberRounder.roundFloat(response.getAsJsonPrimitive("l").getAsFloat(),2);
        float previousPrice =  NumberRounder.roundFloat(response.getAsJsonPrimitive("pc").getAsFloat(),2);
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
