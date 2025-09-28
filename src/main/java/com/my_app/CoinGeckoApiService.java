package com.my_app;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CoinGeckoApiService {

    private final HttpClient client = HttpClient.newHttpClient();
    private static final String API_URL_FORMAT = "https://api.coingecko.com/api/v3/coins/%s/market_chart?vs_currency=usd&days=14&interval=daily";

    public String fetchPriceHistory(String cryptoId) throws IOException, InterruptedException {
        String apiUrl = String.format(API_URL_FORMAT, cryptoId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Gagal mengambil data dari API: " + response.statusCode());
        }
        return response.body();
    }
}