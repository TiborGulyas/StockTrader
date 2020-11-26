package com.codecool.stocktrader.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.springframework.dao.DataAccessException;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class StockPurchase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Stock stock;
    private double purchasePrice;
    private int quantity;
    private Date purchaseDate;

    @JsonBackReference
    @ToString.Exclude
    @ManyToOne
    private UserAccount userAccount;


}
