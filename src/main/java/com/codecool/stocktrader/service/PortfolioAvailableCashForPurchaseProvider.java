package com.codecool.stocktrader.service;


import com.codecool.stocktrader.model.Offer;
import com.codecool.stocktrader.model.UserAccount;
import org.springframework.stereotype.Component;


@Component
public class PortfolioAvailableCashForPurchaseProvider {
    public double providePortfolioAvailableCashForPurchase(UserAccount userAccount){
        double availableCash = userAccount.getCash();
        double cashInOffers = userAccount.getOffers().stream().mapToDouble(Offer::getTotalValue).sum();
        return availableCash-cashInOffers;
    }
}
