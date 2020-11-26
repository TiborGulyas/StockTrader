package com.codecool.stocktrader.controller;

import com.codecool.stocktrader.component.ApiCall;
import com.codecool.stocktrader.model.*;
import com.codecool.stocktrader.repository.CandleRepository;
import com.codecool.stocktrader.repository.OfferRepository;
import com.codecool.stocktrader.repository.StockRepository;
import com.codecool.stocktrader.repository.UserAccountRepository;
import com.codecool.stocktrader.service.ApiStringProvider;
import com.codecool.stocktrader.service.CandlePersister;
import com.codecool.stocktrader.service.OfferTypeProvider;
import com.codecool.stocktrader.service.ResolutionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.google.gson.*;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/stock")
public class StockController {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private CandleRepository candleRepository;

    @Autowired
    private ResolutionProvider resolutionProvider;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private OfferTypeProvider offerTypeProvider;



    @GetMapping("/getcandle/{symbol}/{resolution}")
    public ReactCandleContainer returnIntraday(@PathVariable("symbol") String symbol, @PathVariable("resolution") String res) {

        Resolution resolution = resolutionProvider.createResolution(res);
        CandleContainer candleReturn = candleRepository.findBySymbolAndResolution(symbol, resolution);

        //Reformat DATA for React
        ReactCandleContainer reactCandleContainer = new ReactCandleContainer();
        List<CandleData> candleDataList = candleReturn.getCandleDataList();
        for (CandleData candleData: candleDataList) {
            Double[] candlePrices = new Double[4];
            candlePrices[0] = candleData.getOpenPrice();
            candlePrices[1] = candleData.getHighPrice();
            candlePrices[2] = candleData.getLowPrice();
            candlePrices[3] = candleData.getClosePrice();

            ReactCandleData reactCandleData = ReactCandleData.builder()
                    .x(candleData.getDate())
                    .y(candlePrices)
                    .build();
            reactCandleContainer.getReactCandleDataList().add(reactCandleData);
        }
        return reactCandleContainer;

    }

    @GetMapping("/getquote/{symbol}")
    public Stock returnCurrentPrice(@PathVariable("symbol") String symbol) throws IOException {

        return stockRepository.findBySymbol(symbol);
    }

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
