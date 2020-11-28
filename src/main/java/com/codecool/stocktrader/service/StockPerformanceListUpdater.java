package com.codecool.stocktrader.service;

import com.codecool.stocktrader.model.*;
import com.codecool.stocktrader.repository.LastPriceRepository;
import com.codecool.stocktrader.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class StockPerformanceListUpdater {

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    LastPriceRepository lastPriceRepository;

    public void updateStockPerformanceList(UserAccount userAccount){
        List<StockPurchase> portfolio = userAccount.getPortfolio();
        List<StockPerformance> stockPerformanceList = userAccount.getStockPerformanceList();
        stockPerformanceList.clear();
        Set<Stock> stocksPurchased = new HashSet<>();
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
    }
}
