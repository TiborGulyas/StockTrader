package com.codecool.stocktrader.service;

import com.codecool.stocktrader.model.OfferType;
import org.springframework.stereotype.Component;

@Component
public class OfferTypeProvider {
    public OfferType createOfferType(String offerType){
        if (offerType.equals("BUY")){
            return OfferType.BUY;
        } else if (offerType.equals("SELL")){
            return OfferType.SELL;
        }
        return null;
    }
}
