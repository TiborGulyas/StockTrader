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
@Entity
public class PortfolioPerformance {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private double portfolioTotalValue = 0;
    private double portfolioTotalStockValue = 0;
    private double percentageStockValue = 0;
    private double percentageCashValue = 0;


}
