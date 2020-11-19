package com.codecool.stocktrader.controller;

import com.codecool.stocktrader.component.ApiCall;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/getquote")
public class StockController {

    @Autowired
    private ApiCall apiCall;

    private final String basicAPIEndpoint = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=IBM&interval=1min&outputsize=full&apikey=KSIT3ZSPQP1NLNY4";

    Gson googleJson = new Gson();

    @GetMapping("/")
    public void returnIntraday() throws IOException, JSONException {
        JsonObject result = apiCall.getResult(basicAPIEndpoint);
        System.out.println(result.get("Time Series (1min)"));

        JsonObject array = result.getAsJsonObject("Time Series (1min)");
        Set<Map.Entry<String, JsonElement>> entries = array.entrySet();
        for (Map.Entry e : entries) {
            System.out.println(e);
        }


    }
}
