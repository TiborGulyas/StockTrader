package com.codecool.stocktrader.service;

import com.codecool.stocktrader.model.CandleContainer;
import com.codecool.stocktrader.model.CandleData;
import com.codecool.stocktrader.model.Resolution;
import com.codecool.stocktrader.model.Stock;
import com.codecool.stocktrader.repository.CandleRepository;
import com.codecool.stocktrader.repository.StockRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;


@Component
public class CandlePersister {

    @Autowired
    private ResolutionProvider resolutionProvider;

    @Autowired
    private CandleRepository candleRepository;

    @Autowired
    private StockRepository stockRepository;

    public void persistCandle(JsonObject response, String symbol, String res) {
        CandleContainer candleContainer;
        Stock stock = stockRepository.findBySymbol(symbol);
        Resolution resolution = resolutionProvider.createResolution(res);

        JsonArray closePrices = response.getAsJsonArray("c");
        JsonArray openPrices = response.getAsJsonArray("o");
        JsonArray highPrices = response.getAsJsonArray("h");
        JsonArray lowPrices = response.getAsJsonArray("l");
        JsonArray volumes = response.getAsJsonArray("v");
        JsonArray timeStamps = response.getAsJsonArray("t");


        if (candleRepository.findByStockAndResolution(stock, resolution) != null){
            candleContainer = candleRepository.findByStockAndResolution(stock, resolution);
        } else {
            candleContainer = CandleContainer.builder()
                    .symbol(symbol)
                    .resolution(resolution)
                    .starterTimeStamp(timeStamps.get(0).getAsLong() * 1000)
                    .candleDataList(new ArrayList<>())
                    .stock(stock)
                    .build();
        }


        for (int i = 0; i < closePrices.size(); i++) {
            double closePrice = NumberRounder.roundDouble(closePrices.get(i).getAsDouble(),2);
            double openPrice = NumberRounder.roundDouble(openPrices.get(i).getAsDouble(),2);
            double highPrice = NumberRounder.roundDouble(highPrices.get(i).getAsDouble(),2);
            double lowPrice = NumberRounder.roundDouble(lowPrices.get(i).getAsDouble(),2);
            int volume = volumes.get(i).getAsInt();
            long timeStamp = timeStamps.get(i).getAsLong() * 1000;
            Date date = new Date(timeStamp);
            CandleData candleData = CandleData.builder()
                    .closePrice(closePrice)
                    .openPrice(openPrice)
                    .highPrice(highPrice)
                    .lowPrice(lowPrice)
                    .volume(volume)
                    .timeStamp(timeStamp)
                    .date(date)
                    .candleContainer(candleContainer)
                    .build();
            candleContainer.getCandleDataList().add(candleData);
        }
        System.out.println("candleContainer to be persisted: "+candleContainer.toString());
        candleRepository.save(candleContainer);

        /*
        long originalCandleContainerId = 0;
        if (resolution == Resolution.MIN5) {
            if (candleRepository.findByStockAndResolution(stock, resolution) != null) {
                CandleContainer candleContainer = candleRepository.findByStockAndResolution(stock, resolution);
                List<CandleData> = candleContainer
                originalCandleContainerId = stock.getCandleContainer5Min().getId();
                stock.setCandleContainer5Min(candleContainer);
                stockRepository.save(stock);
                candleRepository.deleteById(originalCandleContainerId);
            } else {
                stock.setCandleContainer5Min(candleContainer);
                stockRepository.save(stock);
            }
        } else if (resolution == Resolution.DAY) {
            if (stock.getCandleContainerDay() != null) {
                originalCandleContainerId = stock.getCandleContainerDay().getId();
                stock.setCandleContainerDay(candleContainer);
                stockRepository.save(stock);
                candleRepository.deleteById(originalCandleContainerId);
            } else {
                stock.setCandleContainerDay(candleContainer);
                stockRepository.save(stock);
            }
        }
        */
    }
}
