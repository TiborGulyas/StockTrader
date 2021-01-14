package com.codecool.stocktrader.service;

import com.codecool.stocktrader.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReactCandleDataConverter {
    public void convertCandleData(CandleContainer candleReturn, Resolution resolution, ReactCandleContainer reactCandleContainer){

        List<CandleData> candleDataList = candleReturn.getCandleDataList();
        for (CandleData candleData: candleDataList) {
            Double[] candlePrices = new Double[4];
            candlePrices[0] = NumberRounder.roundDouble(candleData.getOpenPrice(),2);
            candlePrices[1] = NumberRounder.roundDouble(candleData.getHighPrice(),2);
            candlePrices[2] = NumberRounder.roundDouble(candleData.getLowPrice(),2);
            candlePrices[3] = NumberRounder.roundDouble(candleData.getClosePrice(),2);

            ReactCandleData reactCandleData = ReactCandleData.builder()
                    //.x(candleData.getDate())
                    //.x((double) candleData.getDate().getTime())
                    .y(candlePrices)
                    .build();

            List<Double> volumeData = new ArrayList<>(2);
            volumeData.add(0, (double) candleData.getDate().getTime());
            volumeData.add(1, (double) candleData.getVolume());

            switch (resolution) {
                case MIN5:
                    reactCandleContainer.getReactCandle5().getReactCandleDataList().add(reactCandleData);
                    reactCandleContainer.getReactCandle5().getReactVolumeDataList().add(volumeData);
                    break;
                case MIN1:
                    reactCandleContainer.getReactCandle1().getReactCandleDataList().add(reactCandleData);
                    reactCandleContainer.getReactCandle1().getReactVolumeDataList().add(volumeData);
                    break;
                case DAY:
                    reactCandleContainer.getReactCandleD().getReactCandleDataList().add(reactCandleData);
                    reactCandleContainer.getReactCandleD().getReactVolumeDataList().add(volumeData);
            }
        }
    }
}
