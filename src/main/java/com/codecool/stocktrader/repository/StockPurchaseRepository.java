package com.codecool.stocktrader.repository;

import com.codecool.stocktrader.model.Stock;
import com.codecool.stocktrader.model.StockPurchase;
import com.codecool.stocktrader.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockPurchaseRepository extends JpaRepository<StockPurchase, Long> {
    List<StockPurchase> findAllByStockAndUserAccount(Stock stock, UserAccount userAccount);
}
