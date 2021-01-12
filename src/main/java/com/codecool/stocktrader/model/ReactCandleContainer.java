package com.codecool.stocktrader.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReactCandleContainer {
    private List<ReactCandleData> reactCandleDataList = new ArrayList<>();
    private List<ReactVolumeData> reactVolumeDataList = new ArrayList<>();

}
