package com.codecool.stocktrader.service;

import com.codecool.stocktrader.model.*;
import com.codecool.stocktrader.repository.LastPriceRepository;
import com.codecool.stocktrader.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class StockPerformanceListUpdater {

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    LastPriceRepository lastPriceRepository;

    public List<StockPerformance> getStockPerformanceList(UserAccount userAccount){
        List<StockPurchase> portfolio = userAccount.getPortfolio();
        //List<StockPerformance> stockPerformanceList = new ArrayList<>();
        //stockPerformanceList.clear();

        HashMap<Stock, StockPerformance> stockPerformanceMap = new HashMap<>();

        for (StockPurchase stockPurchase:portfolio) {
            boolean isAbsent = false;
            Stock stock = stockPurchase.getStock();
            LastPrice lastPrice = lastPriceRepository.findByStock(stock);
            int currentPurchaseQuantity = stockPurchase.getQuantity();
            double currentPurchasePrice = stockPurchase.getPurchasePrice();
            double currentPurchaseValue = NumberRounder.roundDouble(currentPurchasePrice*currentPurchaseQuantity, 2);

            stockPerformanceMap.computeIfAbsent(stock, s ->
                StockPerformance.builder()
                        .stock(stock)
                        .build()
                /*
                StockPerformance.builder()
                    .stock(stock)
                    //.userAccount(userAccount)
                    .stockTotalAmount(currentPurchaseQuantity)
                    .totalPurchaseValue(NumberRounder.roundDouble(currentPurchaseValue,2))
                    .averagePurchasePrice(NumberRounder.roundDouble(currentPurchasePrice,2))
                    .stockCurrentPrice(NumberRounder.roundDouble(lastPrice.getCurrentPrice(),2))
                    .stockCurrentValue(NumberRounder.roundDouble(currentPurchaseQuantity*lastPrice.getCurrentPrice(),2))
                    .stockValueChange(NumberRounder.roundDouble(0,4))
                    .build()

                 */
            );

            System.out.println("StockPerformance after IfAbsent:");
            System.out.println(stockPerformanceMap.toString());

            stockPerformanceMap.computeIfPresent(stock, (stockKey, stockPerformance) ->
                StockPerformance.builder()
                    .stock(stockPerformance.getStock())
                    //.userAccount(stockPerformance.getUserAccount())
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
                //.id((long) 0)
                .stock(stock)
                //.userAccount(userAccount)
                .totalPurchaseValue(0)
                .averagePurchasePrice(0)
                .stockCurrentPrice(0)
                .stockCurrentValue(0)
                .stockTotalAmount(0)
                .stockValueChange(0)
                .build()
        );

        for (StockPurchase stockPurchase:portfolio) {
            LastPrice lastPrice = lastPriceRepository.findByStock(stockPurchase.getStock());
            int currentPurchaseQuantity = stockPurchase.getQuantity();
            double currentPurchasePrice = stockPurchase.getPurchasePrice();
            double currentPurchaseValue = NumberRounder.roundDouble(currentPurchasePrice*currentPurchaseQuantity, 2);

            stockPerformanceMap.computeIfPresent(stock, (stockKey, stockPerformance) ->
                    StockPerformance.builder()
                            .stock(stockPerformance.getStock())
                            //.userAccount(stockPerformance.getUserAccount())
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


        /*
        Set<Stock> stocksPurchased = portfolio.stream().map(StockPurchase::getStock).collect(Collectors.toSet());

        for (StockPurchase stockPurchase: portfolio) {
            stocksPurchased.add(stockPurchase.getStock());
        }



        for (Stock stock:stocksPurchased) {
            List<StockPurchase> stockPortfolio = portfolio.stream()
                    .filter(p -> p.getStock() == stock).collect(Collectors.toList());

            LastPrice lastPrice = lastPriceRepository.findByStock(stock);
            int totalAmount = 0;
            double totalPurchaseValue = 0;
            double averagePurchasePrice = 0;
            double currentPrice = 0;
            double currentValue = 0;
            double valueChange = 0;

            for (StockPurchase stockPurchase: stockPortfolio) {
                totalAmount += stockPurchase.getQuantity();
                totalPurchaseValue += NumberRounder.roundDouble(stockPurchase.getQuantity()*stockPurchase.getPurchasePrice(),2);
            }
            averagePurchasePrice = NumberRounder.roundDouble(totalPurchaseValue/totalAmount,2);
            currentPrice = NumberRounder.roundDouble(lastPrice.getCurrentPrice(),2);
            currentValue = NumberRounder.roundDouble(totalAmount*currentPrice,2);
            valueChange = NumberRounder.roundDouble((currentValue/totalPurchaseValue)-1,2);

            StockPerformance stockPerformance = StockPerformance.builder()
                    .stock(stock)
                    .userAccount(userAccount)
                    .stockTotalAmount(totalAmount)
                    .totalPurchaseValue(totalPurchaseValue)
                    .averagePurchasePrice(averagePurchasePrice)
                    .stockCurrentPrice(currentPrice)
                    .stockCurrentValue(currentValue)
                    .stockValueChange(valueChange)
                    .build();
            stockPerformanceList.add(stockPerformance);

        }
        userAccount.setStockPerformanceList(stockPerformanceList);
        userAccountRepository.save(userAccount);
        */

}
