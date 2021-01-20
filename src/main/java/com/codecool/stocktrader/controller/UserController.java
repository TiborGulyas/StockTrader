package com.codecool.stocktrader.controller;

import com.codecool.stocktrader.model.*;
import com.codecool.stocktrader.repository.OfferRepository;
import com.codecool.stocktrader.repository.StockRepository;
import com.codecool.stocktrader.repository.UserAccountRepository;
import com.codecool.stocktrader.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private OfferTypeProvider offerTypeProvider;

    @Autowired
    OfferScanner offerScanner;

    @Autowired
    StockPerformanceListUpdater stockPerformanceListUpdater;

    @Autowired
    PortfolioPerformanceUpdater portfolioPerformanceUpdater;

    @Autowired
    PortfolioAvailableStockQuantityProvider portfolioAvailableStockQuantityProvider;

    @Autowired
    PortfolioAvailableCashForPurchaseProvider portfolioAvailableCashForPurchaseProvider;

    @PostMapping("/placeoffer/{symbol}/{offerType}/{quantity}/{price}")
    public String placeOffer(@PathVariable("symbol") String symbol, @PathVariable("offerType") String offerType, @PathVariable("quantity") int quantity, @PathVariable("price") float price){
        boolean approvalQuantity = false;
        boolean approvalCash = false;
        double newOfferTotalValue = price*quantity;
        UserAccount userAccount = userAccountRepository.findByNickName("Mr.T");
        Stock stock = stockRepository.findBySymbol(symbol);
        OfferType offerTypeObj = offerTypeProvider.createOfferType(offerType);

        //in case of SELL: QUANTITY CHECK
        if (offerTypeObj == OfferType.SELL){
            int stockAvailable = portfolioAvailableStockQuantityProvider.providePortfolioStockQuantity(userAccount, stock);
            if ( stockAvailable >= quantity){
                approvalQuantity = true;
            } else {
                return "ERROR! Stock available: "+stockAvailable+" pcs; Stock requested to sell: "+quantity+" pcs.";
            }
            approvalCash = true;

        } else if (offerTypeObj == OfferType.BUY){
            double cashAvailable = portfolioAvailableCashForPurchaseProvider.providePortfolioAvailableCashForPurchase(userAccount);
            if (cashAvailable >= newOfferTotalValue){
                approvalCash = true;
            } else {
                return "ERROR! Cash available: $ "+cashAvailable+"; Stock value requested to buy: $ "+newOfferTotalValue+".";
            }
            approvalQuantity = true;
        }

        if (approvalQuantity && approvalCash){
            Offer offer = Offer.builder()
                    .offerDate(Calendar.getInstance().getTime())
                    .offerType(offerTypeObj)
                    .price(NumberRounder.roundFloat(price,2))
                    .quantity(quantity)
                    .totalValue(quantity*price)
                    .stock(stock)
                    .userAccount(userAccount)
                    .build();
            userAccount.getOffers().add(offer);
            userAccountRepository.save(userAccount);
            offerScanner.matchUserOffers();
        }
        return "OK";
    }

    @PostMapping("/replaceoffer/{id}/{symbol}/{offerType}/{quantity}/{price}")
    public String replaceOffer(@PathVariable("id") Long id, @PathVariable("symbol") String symbol, @PathVariable("offerType") String offerType, @PathVariable("quantity") int quantity, @PathVariable("price") float price){
        boolean approvalQuantity = false;
        boolean approvalCash = false;
        double newOfferTotalValue = price*quantity;
        UserAccount userAccount = userAccountRepository.findByNickName("Mr.T");
        OfferType newOfferType = offerTypeProvider.createOfferType(offerType);
        Stock stock = stockRepository.findBySymbol(symbol);
        Offer offer = offerRepository.getOne(id);
        double originalOfferTotalValue = offer.getTotalValue();
        double originalOfferQuantity = offer.getQuantity();

        //in case of SELL: QUANTITY CHECK
        if (newOfferType == OfferType.SELL){
            double stockAvailable = portfolioAvailableStockQuantityProvider.providePortfolioStockQuantity(userAccount, stock)+originalOfferQuantity;
            if (stockAvailable >= quantity){
                approvalQuantity = true;
            } else {
                return "ERROR! Stock available: "+stockAvailable+" pcs; Stock requested to sell: "+quantity+" pcs.";
            }
            approvalCash = true;
        } else if (newOfferType == OfferType.BUY){
            double cashAvailable = portfolioAvailableCashForPurchaseProvider.providePortfolioAvailableCashForPurchase(userAccount) + originalOfferTotalValue;
            if (cashAvailable >= newOfferTotalValue){
                approvalCash = true;
            } else {
                return "ERROR! Cash available: $ "+cashAvailable+"; Stock value requested to buy: $ "+newOfferTotalValue+".";
            }
            approvalQuantity = true;
        }

        if (approvalQuantity && approvalCash) {
            if (offer.getStock().getSymbol().equals(symbol)) {
                offer.setOfferType(newOfferType);
                offer.setPrice(price);
                offer.setQuantity(quantity);
                offer.setTotalValue(quantity*price);
                offer.setOfferDate(Calendar.getInstance().getTime());
            }
            offerRepository.save(offer);
            offerScanner.matchUserOffers();
        }
        return "OK";
    }


    @DeleteMapping("/deleteoffer/{id}")
    public void deleteOffer(@PathVariable("id") long id){
        UserAccount defaultUserAccount = userAccountRepository.findByNickName("Mr.T");
        List<Offer> userOffers = defaultUserAccount.getOffers();
        for (Offer offer: userOffers) {
            if (offer.getId() == id){
                userOffers.remove(offer);
                userAccountRepository.save(defaultUserAccount);
                break;
            }
        }
    }

    @GetMapping("getuseraccount")
    public UserAccount getUserAccount(){
        UserAccount defaultUserAccount = userAccountRepository.findByNickName("Mr.T");
        stockPerformanceListUpdater.updateStockPerformanceList(defaultUserAccount);
        portfolioPerformanceUpdater.updatePortfolioPerformance(defaultUserAccount);

        return userAccountRepository.findByNickName("Mr.T");
    }

    @GetMapping("getalloffers")
    public List<Offer> getAllOffers(){
        UserAccount defaultUserAccount = userAccountRepository.findByNickName("Mr.T");
        return defaultUserAccount.getOffers();
    }

    @GetMapping("getoffers/{stock}")
    public List<Offer> getOffersPerStock(@PathVariable("stock") String stock){
        UserAccount defaultUserAccount = userAccountRepository.findByNickName("Mr.T");
        return defaultUserAccount.getOffers().stream().filter(offer -> offer.getStock().getSymbol().equals(stock)).collect(Collectors.toList());
    }

    @GetMapping("getStockPerformanceList")
    public List<StockPerformance> getStockPerformanceList(){
        UserAccount defaultUserAccount = userAccountRepository.findByNickName("Mr.T");
        stockPerformanceListUpdater.updateStockPerformanceList(defaultUserAccount);
        return userAccountRepository.findByNickName("Mr.T").getStockPerformanceList();
    }

    @GetMapping("getStockPerformanceList/{stock}")
    public Optional<StockPerformance> getStockPerformanceListPerStock(@PathVariable("stock") String stock){
        UserAccount defaultUserAccount = userAccountRepository.findByNickName("Mr.T");
        stockPerformanceListUpdater.updateStockPerformanceList(defaultUserAccount);
        return userAccountRepository.findByNickName("Mr.T").getStockPerformanceList().stream().filter(stockPerformance -> stockPerformance.getStock().getSymbol().equals(stock)).findFirst();
    }


    @GetMapping("getportfolioperformance")
    public PortfolioPerformance getPortfolioPerformance(){
        UserAccount defaultUserAccount = userAccountRepository.findByNickName("Mr.T");
        portfolioPerformanceUpdater.updatePortfolioPerformance(defaultUserAccount);
        return userAccountRepository.findByNickName("Mr.T").getPortfolioPerformance();
    }

    @GetMapping("getprofileinfo")
    public ProfileInformation getProfileInfo(){
        UserAccount defaultUserAccount = userAccountRepository.findByNickName("Mr.T");

        return ProfileInformation.builder()
                .profilePic(defaultUserAccount.getProfilePic_())
                .dateOfRegistration(defaultUserAccount.getDateOfRegistration())
                .id(defaultUserAccount.getId())
                .username(defaultUserAccount.getUsername())
                .nickName(defaultUserAccount.getNickName())
                .eMail(defaultUserAccount.getEMail_())
                .build();
    }

}
