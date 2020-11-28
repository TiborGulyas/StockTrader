package com.codecool.stocktrader.controller;

import com.codecool.stocktrader.model.*;
import com.codecool.stocktrader.repository.CandleRepository;
import com.codecool.stocktrader.repository.LastPriceRepository;
import com.codecool.stocktrader.repository.StockRepository;
import com.codecool.stocktrader.service.NumberRounder;
import com.codecool.stocktrader.service.ResolutionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/stock")
public class StockController {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private CandleRepository candleRepository;

    @Autowired
    private ResolutionProvider resolutionProvider;

    @Autowired
    private LastPriceRepository lastPriceRepository;


    @GetMapping("/getcandle/{symbol}/{resolution}")
    public ReactCandleContainer returnIntraday(@PathVariable("symbol") String symbol, @PathVariable("resolution") String res) {
        Stock stock = stockRepository.findBySymbol(symbol);
        Resolution resolution = resolutionProvider.createResolution(res);
        CandleContainer candleReturn = candleRepository.findByStockAndResolution(stock, resolution);

        //Reformat DATA for React
        ReactCandleContainer reactCandleContainer = new ReactCandleContainer();
        List<CandleData> candleDataList = candleReturn.getCandleDataList();
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
            reactCandleContainer.getReactCandleDataList().add(reactCandleData);
        }
        return reactCandleContainer;

    }

    @GetMapping("/getquote/{symbol}")
    public LastPrice returnCurrentPrice(@PathVariable("symbol") String symbol) {
        Stock stock = stockRepository.findBySymbol(symbol);
        return lastPriceRepository.findByStock(stock);
    }





}
