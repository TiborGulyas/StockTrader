package com.codecool.stocktrader.service;

import com.codecool.stocktrader.model.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PortfolioAvailableStockQuantityProvider {
    public int providePortfolioStockQuantity(UserAccount userAccount, Stock stock){
        Integer quantityOfPurchasedStocks = userAccount.getPortfolio().stream().filter(s -> s.getStock() == stock).mapToInt(StockPurchase::getQuantity).sum();
        Integer quantityOfStockOffers = userAccount.getOffers().stream().filter(s -> s.getStock() == stock && s.getOfferType() == OfferType.SELL).mapToInt(Offer::getQuantity).sum();

        /*
        List<StockPerformance> stockPerformance = userAccount.getStockPerformanceList().stream().filter(s -> s.getStock() == stock).collect(Collectors.toList());
        int quantityOfStockInOffers = 0;
        quantityOfStockInOffers = userAccount.getOffers().stream().filter(s -> s.getStock() == stock && s.getOfferType() == OfferType.SELL).mapToInt(Offer::getQuantity).sum();
        */

        if (quantityOfPurchasedStocks > 0){
            return quantityOfPurchasedStocks - quantityOfStockOffers;
        }
        else {
            return 0;
        }
    }
}
