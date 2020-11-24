package com.codecool.stocktrader.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private int capital;

    @JsonManagedReference
    @Builder.Default
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "userAccount", cascade = {CascadeType.ALL})
    List<StockPurchase> stockPurchaseList = new ArrayList<>();
}
