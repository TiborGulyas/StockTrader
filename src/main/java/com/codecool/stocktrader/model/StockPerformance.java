package com.codecool.stocktrader.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class StockPerformance {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Stock stock; // maga a stock model
    private int stockTotalAmount; // pieces
    private double averagePurchasePrice;
    private double totalPurchaseValue; //hány $-ért vetted a pieces-eket összesen
    private double stockCurrentPrice;
    private double stockCurrentValue;
    private double stockValueChange;

    @JsonBackReference
    @ToString.Exclude
    @ManyToOne
    private UserAccount userAccount;
}
