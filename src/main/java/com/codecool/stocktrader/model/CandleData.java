package com.codecool.stocktrader.model;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class CandleData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private double openPrice;
    private double closePrice;
    private double highPrice;
    private double lowPrice;
    private int volume;
    private long timeStamp;
    private Date date;

    @JsonBackReference
    @ToString.Exclude
    @ManyToOne
    private CandleContainer candleContainer;

}
