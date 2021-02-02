package com.codecool.stocktrader.service;

import com.codecool.stocktrader.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class ReactCandleDataConverter {
    public void convertCandleData(CandleContainer candleReturn, Resolution resolution, ReactCandleContainer reactCandleContainer){

        List<CandleData> candleDataList = candleReturn.getCandleDataList();
        short maxCandleDataItem1min = 331;
        short maxCandleDataItem5min = 71;

        for (CandleData candleData: candleDataList) {
            Double[] candlePrices = new Double[4];
            candlePrices[0] = NumberRounder.roundDouble(candleData.getOpenPrice(),2);
            candlePrices[1] = NumberRounder.roundDouble(candleData.getHighPrice(),2);
            candlePrices[2] = NumberRounder.roundDouble(candleData.getLowPrice(),2);
            candlePrices[3] = NumberRounder.roundDouble(candleData.getClosePrice(),2);

            ReactCandleData reactCandleData = ReactCandleData.builder()
                    .x(candleData.getDate())
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

        switch (resolution) {
            case MIN1:
                double lastClosePriceMin1 = reactCandleContainer.getReactCandle1().getReactCandleDataList().get(reactCandleContainer.getReactCandle1().getReactCandleDataList().size() - 1).getY()[3];
                Date lastDateMin1 = reactCandleContainer.getReactCandle1().getReactCandleDataList().get(reactCandleContainer.getReactCandle1().getReactCandleDataList().size() - 1).getX();
                Double[] dummyCandlePricesMin1 = new Double[4];
                dummyCandlePricesMin1[0] = NumberRounder.roundDouble(lastClosePriceMin1, 2);
                dummyCandlePricesMin1[1] = NumberRounder.roundDouble(lastClosePriceMin1, 2);
                dummyCandlePricesMin1[2] = NumberRounder.roundDouble(lastClosePriceMin1, 2);
                dummyCandlePricesMin1[3] = NumberRounder.roundDouble(lastClosePriceMin1, 2);

                while (reactCandleContainer.getReactCandle1().getReactCandleDataList().size() < maxCandleDataItem1min) {
                    lastDateMin1 = new Date(lastDateMin1.getTime() + 60000);
                    ReactCandleData dummyReactCandleDataMin1 = ReactCandleData.builder()
                            .x(lastDateMin1)
                            .y(dummyCandlePricesMin1)
                            .build();
                    List<Double> dummyVolumeDataMin1 = new ArrayList<>(2);
                    dummyVolumeDataMin1.add(0, (double) lastDateMin1.getTime());
                    dummyVolumeDataMin1.add(1, (double) 0);
                    reactCandleContainer.getReactCandle1().getReactCandleDataList().add(dummyReactCandleDataMin1);
                    reactCandleContainer.getReactCandle1().getReactVolumeDataList().add(dummyVolumeDataMin1);

                }
                break;
            case MIN5:
                double lastClosePriceMin5 = reactCandleContainer.getReactCandle5().getReactCandleDataList().get(reactCandleContainer.getReactCandle5().getReactCandleDataList().size() - 1).getY()[3];
                Date lastDateMin5 = reactCandleContainer.getReactCandle5().getReactCandleDataList().get(reactCandleContainer.getReactCandle5().getReactCandleDataList().size() - 1).getX();
                Double[] dummyCandlePricesMin5 = new Double[4];
                dummyCandlePricesMin5[0] = NumberRounder.roundDouble(lastClosePriceMin5, 2);
                dummyCandlePricesMin5[1] = NumberRounder.roundDouble(lastClosePriceMin5, 2);
                dummyCandlePricesMin5[2] = NumberRounder.roundDouble(lastClosePriceMin5, 2);
                dummyCandlePricesMin5[3] = NumberRounder.roundDouble(lastClosePriceMin5, 2);

                while (reactCandleContainer.getReactCandle5().getReactCandleDataList().size() < maxCandleDataItem5min) {
                    lastDateMin5 = new Date(lastDateMin5.getTime() + 60000);
                    ReactCandleData dummyReactCandleDataMin5 = ReactCandleData.builder()
                            .x(lastDateMin5)
                            .y(dummyCandlePricesMin5)
                            .build();
                    List<Double> dummyVolumeDataMin5 = new ArrayList<>(2);
                    dummyVolumeDataMin5.add(0, (double) lastDateMin5.getTime());
                    dummyVolumeDataMin5.add(1, (double) 0);
                    reactCandleContainer.getReactCandle5().getReactCandleDataList().add(dummyReactCandleDataMin5);
                    reactCandleContainer.getReactCandle5().getReactVolumeDataList().add(dummyVolumeDataMin5);

                }
                break;
        }
    }
}
