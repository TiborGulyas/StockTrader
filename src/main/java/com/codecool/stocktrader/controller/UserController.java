package com.codecool.stocktrader.controller;

import com.codecool.stocktrader.model.*;
import com.codecool.stocktrader.repository.StockRepository;
import com.codecool.stocktrader.repository.UserAccountRepository;
import com.codecool.stocktrader.service.NumberRounder;
import com.codecool.stocktrader.service.OfferScanner;
import com.codecool.stocktrader.service.OfferTypeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Calendar;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private OfferTypeProvider offerTypeProvider;

    @Autowired
    OfferScanner offerScanner;

    @PostMapping("/placeoffer/{symbol}/{offerType}/{quantity}/{price}")
    public void placeOffer(@PathVariable("symbol") String symbol, @PathVariable("offerType") String offerType, @PathVariable("quantity") int quantity, @PathVariable("price") float price){
        UserAccount defaultUserAccount = userAccountRepository.findByUsername("Mr.T");
        Stock stock = stockRepository.findBySymbol(symbol);
        OfferType offerTypeObj = offerTypeProvider.createOfferType(offerType);
        Offer offer = Offer.builder()
                .offerDate(Calendar.getInstance().getTime())
                .offerType(offerTypeObj)
                .price(NumberRounder.roundFloat(price,2))
                .quantity(quantity)
                .stock(stock)
                .userAccount(defaultUserAccount)
                .build();
        defaultUserAccount.getOffers().add(offer);
        userAccountRepository.save(defaultUserAccount);
        offerScanner.matchUserOffers();
    }

    @DeleteMapping("/deleteoffer/{id}")
    public void deleteOffer(@PathVariable("id") long id){
        UserAccount defaultUserAccount = userAccountRepository.findByUsername("Mr.T");
        List<Offer> userOffers = defaultUserAccount.getOffers();
        for (Offer offer: userOffers) {
            if (offer.getId() == id){
                userOffers.remove(offer);
                userAccountRepository.save(defaultUserAccount);
                break;
            }
        }
    }
}
