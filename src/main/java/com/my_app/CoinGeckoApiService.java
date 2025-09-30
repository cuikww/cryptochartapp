package com.my_app;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CoinGeckoApiService {

    private static final String PRICE_HISTORY_URL_FORMAT = "https://api.coingecko.com/api/v3/coins/%s/market_chart?vs_currency=%s&days=%s&interval=daily";
    private static final String COIN_LIST_URL = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=25&page=1";

    public String fetchPriceHistory(String cryptoId, String currency, String days) throws IOException, URISyntaxException {
        String apiUrl = String.format(PRICE_HISTORY_URL_FORMAT, cryptoId, currency, days);
        return performGetRequest(apiUrl);
    }

    public List<String> fetchCoinList() throws IOException, URISyntaxException {
        String jsonResponse = performGetRequest(COIN_LIST_URL);
        
        JSONArray coinsArray = new JSONArray(jsonResponse);
        List<String> coinIds = new ArrayList<>();
        for (int i = 0; i < coinsArray.length(); i++) {
            JSONObject coin = coinsArray.getJSONObject(i);
            coinIds.add(coin.getString("id"));
        }
        Collections.sort(coinIds);
        return coinIds;
    }

    private String performGetRequest(String urlString) throws IOException, URISyntaxException {
        HttpURLConnection connection = null;
        try {
            URL url = new URI(urlString).toURL();
            
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "MyCryptoApp/1.0");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("Gagal mengambil data dari API. Status Code: " + responseCode);
            }

            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
            
            return response.toString();

        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}