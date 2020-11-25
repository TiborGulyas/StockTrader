package com.codecool.stocktrader.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Stock stock;
    private double price;
    private int quantity;
    private Date offerDate;

    @JsonBackReference
    @ToString.Exclude
    @ManyToOne
    protected UserAccount userAccount;
}
