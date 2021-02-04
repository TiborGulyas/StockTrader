package com.codecool.stocktrader.service;

import com.codecool.stocktrader.model.*;
import com.codecool.stocktrader.repository.LastPriceRepository;
import com.codecool.stocktrader.repository.OfferRepository;
import com.codecool.stocktrader.repository.StockPurchaseRepository;
import com.codecool.stocktrader.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OfferScanner {
    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private StockPurchaseRepository stockPurchaseRepository;

    @Autowired
    private TransactionServices transactionServices;

    @Autowired
    private LastPriceRepository lastPriceRepository;

    public  void matchUserOffers(){

        List<Offer> offerList = offerRepository.findAll();
        matchOffers(offerList);

    }

    public void matchUserOffersPerUser(UserAccount userAccount){
        List<Offer> offerList = offerRepository.findAllByUserAccount(userAccount);
        matchOffers(offerList);
    }

    private void matchOffers(List<Offer> offerList){
        for (Offer offer: offerList) {
            UserAccount userAccount = offer.getUserAccount();
            Stock offerStock = offer.getStock();
            LastPrice lastPrice = lastPriceRepository.findByStock(offerStock);
            OfferType offerType = offer.getOfferType();

            if (offerType == OfferType.BUY && lastPrice.getCurrentPrice() <= offer.getPrice()){
                if (userAccount.getCash() >= transactionServices.getTotalValueOfOffer(offer)){
                    transactionServices.excecutePurchaseOffer(offer);
                }

            } else if (offerType == OfferType.SELL && lastPrice.getCurrentPrice() >= offer.getPrice()){
                List<StockPurchase> stockPurchaseList = stockPurchaseRepository.findAllByStockAndUserAccount(offerStock, userAccount);
                int offerQuantity = offer.getQuantity();
                int totalPurchasedQuantity = transactionServices.getTotalQuantityofStocks(stockPurchaseList);
                if (totalPurchasedQuantity >= offerQuantity){
                    transactionServices.excecuteSalesOffer(offer);
                }
            }
        }
    }
}
