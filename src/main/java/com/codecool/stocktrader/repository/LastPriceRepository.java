package com.codecool.stocktrader.repository;

import com.codecool.stocktrader.model.LastPrice;
import com.codecool.stocktrader.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LastPriceRepository extends JpaRepository<LastPrice, Long> {
    LastPrice findByStock(Stock stock);

}
