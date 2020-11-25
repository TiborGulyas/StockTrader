package com.codecool.stocktrader.component;


import com.codecool.stocktrader.model.Offer;
import com.codecool.stocktrader.model.Stock;
import com.codecool.stocktrader.model.StockPurchase;
import com.codecool.stocktrader.model.UserAccount;
import com.codecool.stocktrader.repository.StockRepository;
import com.codecool.stocktrader.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;

@Component
public class DataInitializer {

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    private StockRepository stockRepository;

    public void initData(){
        System.out.println("init persistance");

        UserAccount userAccount = UserAccount.builder()
                .capital(1000000)
                .username("Mr.T")
                .build();
        Stock stockApple = Stock.builder()
                .symbol("AAPL")
                .name("Apple")
                .build();
        stockRepository.save(stockApple);
        Stock savedAAPL = stockRepository.findBySymbol("AAPL");
        System.out.println(userAccount.toString());
        StockPurchase stockPurchase = StockPurchase.builder()
                .purchaseDate(Calendar.getInstance().getTime())
                .stock(savedAAPL)
                .purchasePrice(150.23)
                .quantity(100)
                .userAccount(userAccount)
                .build();
        userAccount.getPortfolio().add(stockPurchase);
        System.out.println(userAccount.toString());
        userAccountRepository.save(userAccount);


        UserAccount savedUserAccount = userAccountRepository.findByUsername("Mr.T");
        StockPurchase stockPurchase2 = StockPurchase.builder()
                .purchaseDate(Calendar.getInstance().getTime())
                .stock(savedAAPL)
                .purchasePrice(170.23)
                .quantity(200)
                .userAccount(savedUserAccount)
                .build();
        savedUserAccount.getPortfolio().add(stockPurchase2);
        userAccountRepository.save(savedUserAccount);

        UserAccount savedUserAccount2 = userAccountRepository.findByUsername("Mr.T");
        Offer offer = Offer.builder()
                .offerDate(Calendar.getInstance().getTime())
                .price(170.2)
                .quantity(42)
                .stock(stockApple)
                .userAccount(savedUserAccount2)
                .build();
        savedUserAccount2.getOffers().add(offer);
        userAccountRepository.save(savedUserAccount2);
    }
}
