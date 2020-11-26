package com.codecool.stocktrader.controller;

import com.codecool.stocktrader.model.*;
import com.codecool.stocktrader.repository.CandleRepository;
import com.codecool.stocktrader.repository.StockRepository;
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


    @GetMapping("/getcandle/{symbol}/{resolution}")
    public ReactCandleContainer returnIntraday(@PathVariable("symbol") String symbol, @PathVariable("resolution") String res) {

        Resolution resolution = resolutionProvider.createResolution(res);
        CandleContainer candleReturn = candleRepository.findBySymbolAndResolution(symbol, resolution);

        //Reformat DATA for React
        ReactCandleContainer reactCandleContainer = new ReactCandleContainer();
        List<CandleData> candleDataList = candleReturn.getCandleDataList();
        for (CandleData candleData: candleDataList) {
            Double[] candlePrices = new Double[4];
            candlePrices[0] = candleData.getOpenPrice();
            candlePrices[1] = candleData.getHighPrice();
            candlePrices[2] = candleData.getLowPrice();
            candlePrices[3] = candleData.getClosePrice();

            ReactCandleData reactCandleData = ReactCandleData.builder()
                    .x(candleData.getDate())
                    .y(candlePrices)
                    .build();
            reactCandleContainer.getReactCandleDataList().add(reactCandleData);
        }
        return reactCandleContainer;

    }

    @GetMapping("/getquote/{symbol}")
    public Stock returnCurrentPrice(@PathVariable("symbol") String symbol) {
        return stockRepository.findBySymbol(symbol);
    }





}
