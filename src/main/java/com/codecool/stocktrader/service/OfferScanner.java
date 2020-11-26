package com.codecool.stocktrader.service;

import com.codecool.stocktrader.model.Offer;
import com.codecool.stocktrader.model.Stock;
import com.codecool.stocktrader.model.StockPurchase;
import com.codecool.stocktrader.model.UserAccount;
import com.codecool.stocktrader.repository.OfferRepository;
import com.codecool.stocktrader.repository.StockPurchaseRepository;
import com.codecool.stocktrader.repository.StockRepository;
import com.codecool.stocktrader.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.List;

@Component
public class OfferScanner {
    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private OfferRepository offerRepository;

    public  void matchUserOffers(){

        List<Offer> offerList = offerRepository.findAll();
        for (Offer offer: offerList) {
            UserAccount userAccount = offer.getUserAccount();
            Stock offerStock = offer.getStock();
            Stock currentMarketStock = stockRepository.findBySymbol(offerStock.getSymbol());
            if (currentMarketStock.getLastPrice().getCurrentPrice() <= offerStock.getLastPrice().getCurrentPrice()){
                StockPurchase stockPurchase = StockPurchase.builder()
                        .purchasePrice(currentMarketStock.getLastPrice().getCurrentPrice())
                        .purchaseDate(currentMarketStock.getLastPrice().getTimeOfRetrieval())
                        .stock(offerStock)
                        .quantity(offer.getQuantity())
                        .userAccount(userAccount)
                        .build();
                userAccount.getPortfolio().add(stockPurchase);
                userAccount.getOffers().remove(offer);
                userAccountRepository.save(userAccount);
            }

        }
    }
}
