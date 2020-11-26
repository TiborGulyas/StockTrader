package com.codecool.stocktrader.service;

import com.codecool.stocktrader.model.Offer;
import com.codecool.stocktrader.model.Stock;
import com.codecool.stocktrader.model.StockPurchase;
import com.codecool.stocktrader.model.UserAccount;
import com.codecool.stocktrader.repository.StockRepository;
import com.codecool.stocktrader.repository.UserAccountRepository;
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
        return offer.getQuantity()*offer.getPrice();
    }

    public void excecutePurchaseOffer(Offer offer){
        Stock offerStock = offer.getStock();
        double totalValueOfOffer = getTotalValueOfOffer(offer);
        Stock currentMarketStock = stockRepository.findBySymbol(offerStock.getSymbol());
        UserAccount userAccount = offer.getUserAccount();
        double userCapital = userAccount.getCapital();
        StockPurchase stockPurchase = StockPurchase.builder()
                .purchasePrice(currentMarketStock.getLastPrice().getCurrentPrice())
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
        double  totalValueOfOffer = getTotalValueOfOffer(offer);
        int offerQuantity = offer.getQuantity();

        Iterator<StockPurchase> stockPurchaseIterator = stockPurchases.listIterator();
        while (stockPurchaseIterator.hasNext()) {
            StockPurchase stockPurchase = stockPurchaseIterator.next();
            System.out.println("next StockPurchase: "+stockPurchase);
            if (stockPurchase.getQuantity() <= offerQuantity){
                offerQuantity -= stockPurchase.getQuantity();
                System.out.println("SP before: "+stockPurchases);
                stockPurchaseIterator.remove();
                System.out.println("SP after: "+stockPurchases);
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
