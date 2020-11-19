package com.codecool.stocktrader.component;

import com.codecool.stocktrader.component.RemoteUrlReader;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ApiCall {

    @Autowired
    private RemoteUrlReader remoteUrlReader;

    public JsonObject getResult(String apiPath) throws IOException {
        String result = remoteUrlReader.readFromUrl(apiPath);
        JsonObject resultJson = new JsonParser()
                .parse(result)
                .getAsJsonObject();
        return resultJson;
    }
}
