package com.codecool.stocktrader.service;

import com.codecool.stocktrader.model.Offer;
import com.codecool.stocktrader.model.Stock;
import com.codecool.stocktrader.model.StockPurchase;
import com.codecool.stocktrader.model.UserAccount;
import com.codecool.stocktrader.repository.StockRepository;
import com.codecool.stocktrader.repository.UserAccountRepository;
import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Component
public class TransactionServices {
    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    private StockRepository stockRepository;


    public int getTotalQuantityofStocks(List<StockPurchase> stockPurchases){
        int totalQuantity = 0;
        for (StockPurchase stockPurchase:stockPurchases) {
            totalQuantity += stockPurchase.getQuantity();
        }
        return totalQuantity;
    }

    public double getTotalValueOfOffer(Offer offer){
        return  NumberRounder.roundDouble(offer.getQuantity()*offer.getPrice(),2);
    }

    public void excecutePurchaseOffer(Offer offer){
        Stock offerStock = offer.getStock();
        double totalValueOfOffer = NumberRounder.roundDouble(getTotalValueOfOffer(offer),2);
        Stock currentMarketStock = stockRepository.findBySymbol(offerStock.getSymbol());
        UserAccount userAccount = offer.getUserAccount();
        double userCapital = NumberRounder.roundDouble(userAccount.getCapital(),2);
        StockPurchase stockPurchase = StockPurchase.builder()
                .purchasePrice(NumberRounder.roundDouble(currentMarketStock.getLastPrice().getCurrentPrice(),2))
                .purchaseDate(currentMarketStock.getLastPrice().getTimeOfRetrieval())
                .stock(offerStock)
                .quantity(offer.getQuantity())
                .userAccount(userAccount)
                .build();
        userAccount.getPortfolio().add(stockPurchase);
        userAccount.setCapital(userCapital-totalValueOfOffer);
        userAccount.getOffers().remove(offer);
        userAccountRepository.save(userAccount);
    }

    public void excecuteSalesOffer(Offer offer){
        UserAccount userAccount = offer.getUserAccount();
        List<StockPurchase> stockPurchases = userAccount.getPortfolio();
        double  totalValueOfOffer = NumberRounder.roundDouble(getTotalValueOfOffer(offer),2);
        int offerQuantity = offer.getQuantity();

        Iterator<StockPurchase> stockPurchaseIterator = stockPurchases.listIterator();
        while (stockPurchaseIterator.hasNext()) {
            StockPurchase stockPurchase = stockPurchaseIterator.next();
            if (stockPurchase.getQuantity() <= offerQuantity){
                offerQuantity -= stockPurchase.getQuantity();
                stockPurchaseIterator.remove();
                if (offerQuantity == 0){
                    break;
                }
            } else if (stockPurchase.getQuantity() > offerQuantity){
                stockPurchase.setQuantity(stockPurchase.getQuantity()-offerQuantity);
                offerQuantity = 0;
                break;
            }
        }
        if (offerQuantity == 0){
            userAccount.setPortfolio(stockPurchases);
            userAccount.getOffers().remove(offer);
            userAccount.setCapital(userAccount.getCapital()+totalValueOfOffer);
            userAccountRepository.save(userAccount);
        }
    }
}
