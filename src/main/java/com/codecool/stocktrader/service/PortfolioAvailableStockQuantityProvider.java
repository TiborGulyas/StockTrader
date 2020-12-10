package com.codecool.stocktrader.service;

import com.codecool.stocktrader.model.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PortfolioAvailableStockQuantityProvider {
    public int providePortfolioStockQuantity(UserAccount userAccount, Stock stock){
        List<StockPerformance> stockPerformance = userAccount.getStockPerformanceList().stream().filter(s -> s.getStock() == stock).collect(Collectors.toList());
        int quantityOfStockInOffers = 0;
        quantityOfStockInOffers = userAccount.getOffers().stream().filter(s -> s.getStock() == stock && s.getOfferType() == OfferType.SELL).mapToInt(Offer::getQuantity).sum();

        if (stockPerformance.size() == 0){
            return 0;
        }
        else {
            return stockPerformance.get(0).getStockTotalAmount() - quantityOfStockInOffers;
        }
    }
}
