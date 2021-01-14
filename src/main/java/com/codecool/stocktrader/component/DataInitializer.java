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
                .cashInvested(NumberRounder.roundDouble(1000000,2))
                .username("Mr.T")
                .build();


        Stock stockApple = Stock.builder()
                .symbol("AAPL")
                .name("Apple")
                .build();
        stockRepository.save(stockApple);

        /*
        Stock stockTesla = Stock.builder()
                .symbol("TSLA")
                .name("Tesla Inc.")
                .build();
        stockRepository.save(stockTesla);

        Stock stockFacebook = Stock.builder()
                .symbol("FB")
                .name("Facebook")
                .build();
        stockRepository.save(stockFacebook);

        Stock stockGoogle = Stock.builder()
                .symbol("GOOGL")
                .name("Alphabet Inc Class A")
                .build();
        stockRepository.save(stockGoogle);

        Stock stockNvidia = Stock.builder()
                .symbol("NVDA")
                .name("Nvidia")
                .build();
        stockRepository.save(stockNvidia);

        Stock stockZoom = Stock.builder()
                .symbol("ZM")
                .name("Zoom")
                .build();
        stockRepository.save(stockZoom);

        Stock stockBankOfAmerica = Stock.builder()
                .symbol("BAC")
                .name("Bank of America Corp")
                .build();
        stockRepository.save(stockBankOfAmerica);

         */

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

        StockPurchase stockPurchase2 = StockPurchase.builder()
                .purchaseDate(Calendar.getInstance().getTime())
                .stock(savedAAPL)
                .purchasePrice(NumberRounder.roundDouble(170.23,2))
                .quantity(200)
                .userAccount(userAccount)
                .build();
        userAccount.getPortfolio().add(stockPurchase2);

        /*
        Stock savedTSLA = stockRepository.findBySymbol("TSLA");
        StockPurchase stockPurchase3 = StockPurchase.builder()
                .purchaseDate(Calendar.getInstance().getTime())
                .stock(savedTSLA)
                .purchasePrice(NumberRounder.roundDouble(250.44,2))
                .quantity(30)
                .userAccount(userAccount)
                .build();
        userAccount.getPortfolio().add(stockPurchase3);
        */
        System.out.println(userAccount.toString());
        userAccountRepository.save(userAccount);

        /*
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
        */


        UserAccount savedUserAccount2 = userAccountRepository.findByUsername("Mr.T");
        Offer offerAAPL1 = Offer.builder()
                .offerDate(Calendar.getInstance().getTime())
                .offerType(offerTypeProvider.createOfferType("BUY"))
                .price(NumberRounder.roundDouble(170.2,2))
                .quantity(42)
                .totalValue(NumberRounder.roundDouble(170.2*42,2))
                .stock(stockApple)
                .userAccount(savedUserAccount2)
                .build();
        savedUserAccount2.getOffers().add(offerAAPL1);

        Offer offerAAPL2 = Offer.builder()
                .offerDate(Calendar.getInstance().getTime())
                .offerType(offerTypeProvider.createOfferType("BUY"))
                .price(NumberRounder.roundDouble(70.2,2))
                .quantity(102)
                .totalValue(NumberRounder.roundDouble(70.2*102,2))
                .stock(stockApple)
                .userAccount(savedUserAccount2)
                .build();
        savedUserAccount2.getOffers().add(offerAAPL2);

        /*
        Offer offerTSLA1 = Offer.builder()
                .offerDate(Calendar.getInstance().getTime())
                .offerType(offerTypeProvider.createOfferType("BUY"))
                .price(NumberRounder.roundDouble(510.2,2))
                .quantity(15)
                .totalValue(510.2*15)
                .stock(stockTesla)
                .userAccount(savedUserAccount2)
                .build();
        savedUserAccount2.getOffers().add(offerTSLA1);

         */

        userAccountRepository.save(savedUserAccount2);




        //INIT HOLIDAYS CONTAINER

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
