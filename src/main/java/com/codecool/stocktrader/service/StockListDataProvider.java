package com.codecool.stocktrader.service;

import com.codecool.stocktrader.model.*;
import com.codecool.stocktrader.repository.CandleRepository;
import com.codecool.stocktrader.repository.LastPriceRepository;
import com.codecool.stocktrader.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Component
public class StockListDataProvider {

    @Autowired
    LastPriceRepository lastPriceRepository;

    @Autowired
    CandleRepository candleRepository;

    @Autowired
    ResolutionProvider resolutionProvider;


    public List<StockListData> provideStockListData(){
        List<StockListData> stockListAllData = new ArrayList<>();
        List<LastPrice> allLastPrices = lastPriceRepository.findAll();

        for (LastPrice lastPrice : allLastPrices) {
            Stock stock = lastPrice.getStock();
            CandleContainer candleContainer = candleRepository.findAllByStockAndResolution(stock, resolutionProvider.createResolution("D"));
            List<Double> priceData = new ArrayList<>();
            List<Date> dates = new ArrayList<>();
            for (CandleData candleData:candleContainer.getCandleDataList()) {
                priceData.add(candleData.getClosePrice());
                dates.add(candleData.getDate());
            }

            stockListAllData.add(StockListData.builder()
                    .stock(stock)
                    .currentPrice(NumberRounder.roundDouble(lastPrice.getCurrentPrice(),2))
                    .priceChange(NumberRounder.roundDouble((lastPrice.getCurrentPrice()/lastPrice.getPreviousClosePrice()-1)*100, 2))
                    .historicalPrices(priceData)
                    .dates(dates)
                    .build()
            );
        }

        return stockListAllData;


    }
}
