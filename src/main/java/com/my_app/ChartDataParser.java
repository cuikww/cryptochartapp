package com.my_app;

import javafx.scene.chart.XYChart;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class ChartDataParser {

    public XYChart.Series<String, Number> parsePriceHistory(String jsonResponse, String cryptoId) {
        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONArray prices = jsonObject.getJSONArray("prices");

        Map<String, Number> dailyData = new LinkedHashMap<>();  

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
        for (int i = 0; i < prices.length(); i++) {
            JSONArray pricePoint = prices.getJSONArray(i);
            long timestamp = pricePoint.getLong(0);
            double price = pricePoint.getDouble(1);
            String date = sdf.format(new Date(timestamp));
            dailyData.put(date, price);  
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(cryptoId.substring(0, 1).toUpperCase() + cryptoId.substring(1));

        for (Map.Entry<String, Number> entry : dailyData.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        return series;
    }
}