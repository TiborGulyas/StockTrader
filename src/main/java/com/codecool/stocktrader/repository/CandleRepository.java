package com.codecool.stocktrader.repository;

import com.codecool.stocktrader.model.CandleContainer;
import com.codecool.stocktrader.model.Resolution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandleRepository extends JpaRepository<CandleContainer, Long> {

    CandleContainer findBySymbolAndResolution(String symbol, Resolution resolution);

}
