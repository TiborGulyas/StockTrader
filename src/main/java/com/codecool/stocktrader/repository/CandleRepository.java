package com.codecool.stocktrader.repository;

import com.codecool.stocktrader.model.CandleContainer;
import com.codecool.stocktrader.model.Resolution;
import com.codecool.stocktrader.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandleRepository extends JpaRepository<CandleContainer, Long> {


    CandleContainer findByStockAndResolution(Stock stock, Resolution resolution);

}
