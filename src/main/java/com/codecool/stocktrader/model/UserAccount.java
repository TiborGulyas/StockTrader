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
import java.util.Date;
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
    private String nickName;
    @Column(length = 10000)
    private String profilePic_;
    private Date dateOfRegistration;
    private String eMail_;

    private double cash;
    private double cashInvested;


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

    @JsonManagedReference
    @Builder.Default
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "userAccount", cascade = {CascadeType.ALL}, orphanRemoval = true)
    List<StockPerformance> stockPerformanceList = new ArrayList<>();


    @Builder.Default
    @OneToOne(cascade = {CascadeType.ALL}, orphanRemoval = true)
    PortfolioPerformance portfolioPerformance = new PortfolioPerformance();

}
