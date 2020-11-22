package com.codecool.stocktrader.repository;

import com.codecool.stocktrader.model.CandleContainer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandleRepository extends JpaRepository<CandleContainer, Long> {

}
