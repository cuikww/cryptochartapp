package com.my_app;

import javafx.scene.chart.XYChart;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChartDataParser {

    public XYChart.Series<String, Number> parsePriceHistory(String jsonResponse, String cryptoId) {
        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONArray prices = jsonObject.getJSONArray("prices");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(cryptoId.substring(0, 1).toUpperCase() + cryptoId.substring(1));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
        for (int i = 0; i < prices.length(); i++) {
            JSONArray pricePoint = prices.getJSONArray(i);
            long timestamp = pricePoint.getLong(0);
            double price = pricePoint.getDouble(1);
            String date = sdf.format(new Date(timestamp));
            series.getData().add(new XYChart.Data<>(date, price));
        }
        return series;
    }
}