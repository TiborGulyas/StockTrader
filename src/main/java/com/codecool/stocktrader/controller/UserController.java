package com.codecool.stocktrader.controller;

import com.codecool.stocktrader.model.Offer;
import com.codecool.stocktrader.model.Stock;
import com.codecool.stocktrader.model.UserAccount;
import com.codecool.stocktrader.repository.StockRepository;
import com.codecool.stocktrader.repository.UserAccountRepository;
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

    @PostMapping("/placeoffer/{symbol}/{offerType}/{quantity}/{price}")
    public void placeOffer(@PathVariable("symbol") String symbol, @PathVariable("offerType") String offerType, @PathVariable("quantity") int quantity, @PathVariable("price") float price){
        UserAccount defaultUserAccount = userAccountRepository.findByUsername("Mr.T");
        Stock stock = stockRepository.findBySymbol(symbol);
        Offer offer = Offer.builder()
                .offerDate(Calendar.getInstance().getTime())
                .offerType(offerTypeProvider.createOfferType(offerType))
                .price(price)
                .quantity(quantity)
                .stock(stock)
                .userAccount(defaultUserAccount)
                .build();
        defaultUserAccount.getOffers().add(offer);
        userAccountRepository.save(defaultUserAccount);
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
