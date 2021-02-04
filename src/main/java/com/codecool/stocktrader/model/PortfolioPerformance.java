package com.codecool.stocktrader.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PortfolioPerformance {
    //@Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    //private Long id;

    private double investedCash = 0;
    private double portfolioTotalValue = 0;
    private double portfolioTotalStockValue = 0;
    private double portfolioFreeCashValue = 0;
    private double portfolioTotalStockPurchaseValue = 0;
    private double percentageStockValue = 0;
    private double percentageCashValue = 0;
    private double currentStockProfit = 0;
    private double investedCashProfit = 0;
    private double percentageCurrentStockProfit = 0;
    private double percentageInvestedCashProfit = 0;

}
