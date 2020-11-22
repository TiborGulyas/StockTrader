package com.codecool.stocktrader.service;

import com.codecool.stocktrader.model.Resolution;
import org.springframework.stereotype.Component;

@Component
public class ResolutionProvider {
    public Resolution createResolution(String param){
        if (param.equals("1")){
            return Resolution.MIN;
        } else if (param.equals("D")){
            return Resolution.DAY;
        }
        return null;
    }
}
