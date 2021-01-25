package com.codecool.stocktrader.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @Column(unique = true, nullable = false)
    private String symbol;
    private String logo;
    private Date ipo;
    private float sharesOutstanding;
    private String weburl;
    private String exchange;
    private String country;
    private String industry;
    
    //@OneToOne(cascade=CascadeType.ALL)
    //private LastPrice lastPrice;
    //@OneToOne(cascade=CascadeType.ALL)
    //private CandleContainer candleContainer5Min;
    //@OneToOne(cascade=CascadeType.ALL)
    //private CandleContainer candleContainerDay;
}
