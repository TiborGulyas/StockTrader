package com.codecool.stocktrader.component;


import com.codecool.stocktrader.model.*;
import com.codecool.stocktrader.repository.StockRepository;
import com.codecool.stocktrader.repository.UserAccountRepository;
import com.codecool.stocktrader.service.NumberRounder;
import com.codecool.stocktrader.service.OfferTypeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DataInitializer {

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private OfferTypeProvider offerTypeProvider;

    public static final Map<String, List<Map<String, Long>>> tradeHolidays = new HashMap<>();

    public void initData(){
        System.out.println("init persistance");

        UserAccount userAccount = UserAccount.builder()
                .cash(NumberRounder.roundDouble(1000000,2))
                .username("Mr.T")
                .build();
        Stock stockApple = Stock.builder()
                .symbol("AAPL")
                .name("Apple")
                .build();
        stockRepository.save(stockApple);

        Stock stockTesla = Stock.builder()
                .symbol("TSLA")
                .name("Tesla Inc.")
                .build();
        stockRepository.save(stockTesla);

        Stock savedAAPL = stockRepository.findBySymbol("AAPL");
        System.out.println(userAccount.toString());
        StockPurchase stockPurchase = StockPurchase.builder()
                .purchaseDate(Calendar.getInstance().getTime())
                .stock(savedAAPL)
                .purchasePrice(NumberRounder.roundDouble(150.23,2))
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
                .purchasePrice(NumberRounder.roundDouble(170.23,2))
                .quantity(200)
                .userAccount(savedUserAccount)
                .build();
        savedUserAccount.getPortfolio().add(stockPurchase2);
        userAccountRepository.save(savedUserAccount);



        UserAccount savedUserAccount2 = userAccountRepository.findByUsername("Mr.T");
        Offer offer = Offer.builder()
                .offerDate(Calendar.getInstance().getTime())
                .offerType(offerTypeProvider.createOfferType("BUY"))
                .price(NumberRounder.roundDouble(170.2,2))
                .quantity(42)
                .stock(stockApple)
                .userAccount(savedUserAccount2)
                .build();
        savedUserAccount2.getOffers().add(offer);
        userAccountRepository.save(savedUserAccount2);

        //INIT HOLIDAYS CONTAINER
        //Map<String, List<Map<String, Long>>> TradeHolidays = new HashMap<>();

        List<Map<String, Long>> holiday2020_11_26 = new ArrayList<>(2);
        Map<String, Long> holiday2020_11_26_start_end = new HashMap<>();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2020,10,26,15,31,0);
        holiday2020_11_26_start_end.put("start", startDate.getTimeInMillis());
        Calendar endDate = Calendar.getInstance();
        endDate.set(2020,10,27,15,30,59);
        holiday2020_11_26_start_end.put("end", endDate.getTimeInMillis());

        Map<String, Long> UTCTimeStamps = new HashMap<>();
        Calendar fromDate = Calendar.getInstance();
        fromDate.set(2020,10,25,15,30,0);
        UTCTimeStamps.put("from", fromDate.getTimeInMillis()/1000);
        Calendar toDate = Calendar.getInstance();
        toDate.set(2020,10,25,22,0, 0);
        UTCTimeStamps.put("to", toDate.getTimeInMillis()/1000);

        holiday2020_11_26.add(0, holiday2020_11_26_start_end);
        holiday2020_11_26.add(1,UTCTimeStamps);

        tradeHolidays.put("2020_11_26", holiday2020_11_26);
        System.out.println(tradeHolidays.toString());
    }
}
