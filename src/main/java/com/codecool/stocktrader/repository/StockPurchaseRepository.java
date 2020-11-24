package com.codecool.stocktrader.repository;

import com.codecool.stocktrader.model.StockPurchase;
import com.codecool.stocktrader.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockPurchaseRepository extends JpaRepository<StockPurchase, Long> {
}
