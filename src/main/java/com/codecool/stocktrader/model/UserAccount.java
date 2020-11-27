package com.codecool.stocktrader.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;

    private double cash;

    @JsonManagedReference
    @Builder.Default
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "userAccount", cascade = {CascadeType.ALL}, orphanRemoval = true)
    List<StockPurchase> portfolio = new ArrayList<>();

    @JsonManagedReference
    @Builder.Default
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "userAccount", cascade = {CascadeType.ALL}, orphanRemoval = true)
    List<Offer> offers = new ArrayList<>();

    @OneToOne(cascade= CascadeType.ALL)
    private PortfolioSummary portfolioSummary;
}
