package com.my_app;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class CoinGeckoApiService {

    private final HttpClient client = HttpClient.newHttpClient();
    private static final String API_URL_FORMAT = "https://api.coingecko.com/api/v3/coins/%s/market_chart?vs_currency=%s&days=%s&interval=daily";

    public String fetchPriceHistory(String cryptoId, String currency, String days) throws IOException, InterruptedException {
        String apiUrl = String.format(API_URL_FORMAT, cryptoId, currency, days);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Gagal mengambil data dari API: " + response.statusCode());
        }
        return response.body();
    }

    public List<String> fetchCoinList() throws IOException, InterruptedException {
        String apiUrl = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=25&page=1";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Gagal mengambil daftar coin dari API: " + response.statusCode());
        }

        JSONArray coinsArray = new JSONArray(response.body());
        List<String> coinIds = new ArrayList<>();
        for (int i = 0; i < coinsArray.length(); i++) {
            JSONObject coin = coinsArray.getJSONObject(i);
            coinIds.add(coin.getString("id"));
        }
        Collections.sort(coinIds);
        return coinIds;
    }
}