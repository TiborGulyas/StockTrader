package com.codecool.stocktrader.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockListData {
    private Stock stock;
    private double currentPrice;
    private double priceChange;
    private List<Double> historicalPrices;
    private List<Date> dates;
}
