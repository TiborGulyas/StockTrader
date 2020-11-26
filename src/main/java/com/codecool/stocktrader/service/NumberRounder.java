package com.codecool.stocktrader.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberRounder {

    public static Float roundFloat(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d)).setScale(decimalPlace, RoundingMode.HALF_EVEN);
        return bd.floatValue();
    }

    public static Double roundDouble(double d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Double.toString(d)).setScale(decimalPlace, RoundingMode.HALF_EVEN);
        return bd.doubleValue();
    }
}
