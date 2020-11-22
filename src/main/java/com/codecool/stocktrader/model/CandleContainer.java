package com.codecool.stocktrader.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class CandleContainer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String symbol;

    @Enumerated(EnumType.STRING)
    private Resolution resolution;
    private long starterTimeStamp;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "candleContainer", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<CandleData> candleDataList;
}
