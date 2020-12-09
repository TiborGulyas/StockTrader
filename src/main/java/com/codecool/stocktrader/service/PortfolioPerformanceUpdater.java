package com.codecool.stocktrader.service;

import com.codecool.stocktrader.model.PortfolioPerformance;
import com.codecool.stocktrader.model.Stock;
import com.codecool.stocktrader.model.StockPurchase;
import com.codecool.stocktrader.model.UserAccount;
import com.codecool.stocktrader.repository.LastPriceRepository;
import com.codecool.stocktrader.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class PortfolioPerformanceUpdater {

    @Autowired
    LastPriceRepository lastPriceRepository;

    @Autowired
    UserAccountRepository userAccountRepository;

    public void updatePortfolioPerformance(UserAccount userAccount){
        PortfolioPerformance portfolioPerformance = userAccount.getPortfolioPerformance();
        System.out.println("this is the portfolio performance: "+portfolioPerformance);
        List<StockPurchase> portfolio = userAccount.getPortfolio();
        double totalValue = 0;
        double totalStockValue = 0;
        double percentageStockValue = 0;
        double percentageCashValue = 0;
        double currentStockProfit = 0;
        double investedCashProfit = 0;
        double percentageCurrentStockProfit = 0;
        double percentageInvestedCashProfit = 0;


        for (StockPurchase stockPurchase:portfolio) {
            double currentPrice = lastPriceRepository.findByStock(stockPurchase.getStock()).getCurrentPrice();
            totalStockValue += currentPrice*stockPurchase.getQuantity();
            currentStockProfit += (currentPrice-stockPurchase.getPurchasePrice())*stockPurchase.getQuantity();
        }

        totalValue = userAccount.getCash()+totalStockValue;
        percentageStockValue = totalStockValue/totalValue*100;
        percentageCashValue = userAccount.getCash()/totalValue*100;
        investedCashProfit = totalValue - userAccount.getCashInvested();
        percentageCurrentStockProfit = currentStockProfit/totalStockValue*100;
        percentageInvestedCashProfit = (totalValue/userAccount.getCashInvested()-1)*100;

        portfolioPerformance.setPortfolioTotalValue(NumberRounder.roundDouble(totalValue,2));
        portfolioPerformance.setPortfolioTotalStockValue(NumberRounder.roundDouble(totalStockValue,2));
        portfolioPerformance.setPercentageCashValue(NumberRounder.roundDouble(percentageCashValue,2));
        portfolioPerformance.setPercentageStockValue(NumberRounder.roundDouble(percentageStockValue,2));
        portfolioPerformance.setCurrentStockProfit(NumberRounder.roundDouble(currentStockProfit,2));
        portfolioPerformance.setInvestedCashProfit(NumberRounder.roundDouble(investedCashProfit,2));
        portfolioPerformance.setPercentageCurrentStockProfit(NumberRounder.roundDouble(percentageCurrentStockProfit,2));
        portfolioPerformance.setPercentageInvestedCashProfit(NumberRounder.roundDouble(percentageInvestedCashProfit,2));
        userAccountRepository.save(userAccount);

    }
}
