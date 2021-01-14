package com.codecool.stocktrader.repository;

import com.codecool.stocktrader.model.CandleContainer;
import com.codecool.stocktrader.model.CandleData;
import com.codecool.stocktrader.model.Resolution;
import com.codecool.stocktrader.model.Stock;
import org.hibernate.annotations.Persister;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface CandleRepository extends JpaRepository<CandleContainer, Long> {


    CandleContainer findAllByStockAndResolution(Stock stock, Resolution resolution);



}
