package com.codecool.stocktrader.service;

import com.codecool.stocktrader.model.*;
import com.codecool.stocktrader.repository.LastPriceRepository;
import com.codecool.stocktrader.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import java.util.*;


@Component
public class StockPerformanceListUpdater {

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    LastPriceRepository lastPriceRepository;

    public List<StockPerformance> getStockPerformanceList(UserAccount userAccount){
        List<StockPurchase> portfolio = userAccount.getPortfolio();

        HashMap<Stock, StockPerformance> stockPerformanceMap = new HashMap<>();

        for (StockPurchase stockPurchase:portfolio) {
            Stock stock = stockPurchase.getStock();
            LastPrice lastPrice = lastPriceRepository.findByStock(stock);
            int currentPurchaseQuantity = stockPurchase.getQuantity();
            double currentPurchasePrice = stockPurchase.getPurchasePrice();
            double currentPurchaseValue = NumberRounder.roundDouble(currentPurchasePrice*currentPurchaseQuantity, 2);

            stockPerformanceMap.computeIfAbsent(stock, s ->
                StockPerformance.builder()
                        .stock(stock)
                        .build()
            );

            System.out.println("StockPerformance after IfAbsent:");
            System.out.println(stockPerformanceMap.toString());

            stockPerformanceMap.computeIfPresent(stock, (stockKey, stockPerformance) ->
                StockPerformance.builder()
                    .stock(stockPerformance.getStock())
                    .stockTotalAmount(stockPerformance.getStockTotalAmount()+currentPurchaseQuantity)
                    .totalPurchaseValue(NumberRounder.roundDouble( stockPerformance.getTotalPurchaseValue()+currentPurchaseValue,2))
                    .averagePurchasePrice(NumberRounder.roundDouble((stockPerformance.getTotalPurchaseValue()+currentPurchaseValue)/(stockPerformance.getStockTotalAmount()+currentPurchaseQuantity),2))
                    .stockCurrentPrice(NumberRounder.roundDouble(lastPrice.getCurrentPrice(),2))
                    .stockCurrentValue(NumberRounder.roundDouble((stockPerformance.getStockTotalAmount()+currentPurchaseQuantity)*lastPrice.getCurrentPrice(),2))
                    .stockValueChange(NumberRounder.roundDouble(((stockPerformance.getStockTotalAmount()+currentPurchaseQuantity)*lastPrice.getCurrentPrice())/(stockPerformance.getTotalPurchaseValue()+currentPurchaseValue)-1,4))
                    .build()
            );
        }

        return new ArrayList<>(stockPerformanceMap.values());

    }

    public StockPerformance getStockPerformance(UserAccount userAccount, Stock stock){
        List<StockPurchase> portfolio = userAccount.getPortfolio();
        HashMap<Stock, StockPerformance> stockPerformanceMap = new HashMap<>();

        stockPerformanceMap.put(stock, StockPerformance.builder()
                .stock(stock)
                .build()
        );

        for (StockPurchase stockPurchase:portfolio) {
            LastPrice lastPrice = lastPriceRepository.findByStock(stockPurchase.getStock());
            int currentPurchaseQuantity = stockPurchase.getQuantity();
            double currentPurchasePrice = stockPurchase.getPurchasePrice();
            double currentPurchaseValue = NumberRounder.roundDouble(currentPurchasePrice*currentPurchaseQuantity, 2);

            stockPerformanceMap.computeIfPresent(stockPurchase.getStock(), (stockKey, stockPerformance) ->
                    StockPerformance.builder()
                            .stock(stockPerformance.getStock())
                            .stockTotalAmount(stockPerformance.getStockTotalAmount()+currentPurchaseQuantity)
                            .totalPurchaseValue(NumberRounder.roundDouble( stockPerformance.getTotalPurchaseValue()+currentPurchaseValue,2))
                            .averagePurchasePrice(NumberRounder.roundDouble((stockPerformance.getTotalPurchaseValue()+currentPurchaseValue)/(stockPerformance.getStockTotalAmount()+currentPurchaseQuantity),2))
                            .stockCurrentPrice(NumberRounder.roundDouble(lastPrice.getCurrentPrice(),2))
                            .stockCurrentValue(NumberRounder.roundDouble((stockPerformance.getStockTotalAmount()+currentPurchaseQuantity)*lastPrice.getCurrentPrice(),2))
                            .stockValueChange(NumberRounder.roundDouble(((stockPerformance.getStockTotalAmount()+currentPurchaseQuantity)*lastPrice.getCurrentPrice())/(stockPerformance.getTotalPurchaseValue()+currentPurchaseValue)-1,4))
                            .build()
            );
        }

        return stockPerformanceMap.get(stock);
    }
}
