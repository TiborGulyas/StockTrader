package com.codecool.stocktrader.service;

import org.springframework.stereotype.Component;

@Component
public class StockLogoProvider {
    public String provideStockLogo(String stock){
        return "https://logo.clearbit.com/"+stock+"?size=100";
    }
}
