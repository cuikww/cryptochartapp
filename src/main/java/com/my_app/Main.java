package com.my_app;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.io.File;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Main extends Application {

    private MainView mainView;
    private final CoinGeckoApiService apiService = new CoinGeckoApiService();
    private final ChartDataParser dataParser = new ChartDataParser();
    private final ChartImageExporter imageExporter = new ChartImageExporter();
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Crypto Price Chart");

        mainView = new MainView();

        Task<List<String>> fetchCoinListTask = new Task<>() {
            @Override
            protected List<String> call() throws Exception {
                return apiService.fetchCoinList();
            }
        };

        fetchCoinListTask.setOnSucceeded(event -> {
            List<String> coinIds = fetchCoinListTask.getValue();
            mainView.setCryptoItems(coinIds);
            mainView.getStatusLabel().setText("Daftar coin berhasil dimuat.");
        });

        fetchCoinListTask.setOnFailed(event -> {
            List<String> fallback = Arrays.asList("bitcoin", "ethereum", "dogecoin", "solana");
            mainView.setCryptoItems(fallback);
            mainView.getStatusLabel().setText("Gagal memuat daftar coin: " + fetchCoinListTask.getException().getMessage() + ". Menggunakan daftar default.");
        });

        new Thread(fetchCoinListTask).start();

        mainView.getFetchButton().setOnAction(e -> handleFetchData());
        mainView.getDownloadButton().setOnAction(e -> handleDownloadChart());

        Scene scene = new Scene(mainView, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleFetchData() {
        String cryptoId = mainView.getSelectedCrypto();
        String days = mainView.getSelectedTimeRange();
        String currency = mainView.getSelectedCurrency();
        
        Task<XYChart.Series<String, Number>> fetchDataTask = new Task<>() {
            @Override
            protected XYChart.Series<String, Number> call() throws Exception {
                updateMessage("Mengambil data untuk " + cryptoId + " (" + days + " hari, dalam " + currency.toUpperCase() + ")...");
                String jsonResponse = apiService.fetchPriceHistory(cryptoId, currency, days);
                return dataParser.parsePriceHistory(jsonResponse, cryptoId);
            }
        };

        fetchDataTask.setOnSucceeded(event -> {
            mainView.getStatusLabel().textProperty().unbind();
            XYChart.Series<String, Number> series = fetchDataTask.getValue();
            mainView.updateChart(series);
            mainView.updateYAxisLabel(currency);
            String capitalizedCrypto = cryptoId.substring(0, 1).toUpperCase() + cryptoId.substring(1);
            mainView.getLineChart().setTitle("Harga " + capitalizedCrypto + " (" + days + " Hari Terakhir)");
            
            if (series.getData().isEmpty()) {
                mainView.getStatusLabel().setText("Tidak ada data yang tersedia.");
                return;
            }
            
            double minPrice = Double.MAX_VALUE;
            double maxPrice = Double.MIN_VALUE;
            double sumPrice = 0.0;
            int dataCount = series.getData().size();
            
            for (XYChart.Data<String, Number> data : series.getData()) {
                double price = data.getYValue().doubleValue();
                if (price < minPrice) minPrice = price;
                if (price > maxPrice) maxPrice = price;
                sumPrice += price;
            }
            
            double avgPrice = sumPrice / dataCount;
            
            double firstPrice = series.getData().get(0).getYValue().doubleValue();
            double lastPrice = series.getData().get(dataCount - 1).getYValue().doubleValue();
            double percentChange = 0.0;
            if (firstPrice != 0) {
                percentChange = ((lastPrice - firstPrice) / firstPrice) * 100;
            }
            String changeSign = percentChange >= 0 ? "+" : "";
            
            NumberFormat formatter = NumberFormat.getInstance(new Locale("id", "ID"));
            formatter.setMinimumFractionDigits(2);
            formatter.setMaximumFractionDigits(2);
            
            String upperCurrency = currency.toUpperCase();
            String insight = String.format(
                "Harga Min: %s %s | Max: %s %s | Avg: %s %s | Perubahan: %s%.2f%%",
                formatter.format(minPrice), upperCurrency,
                formatter.format(maxPrice), upperCurrency,
                formatter.format(avgPrice), upperCurrency,
                changeSign, percentChange, days
            );
            mainView.getStatusLabel().setText(insight);
            
            mainView.showLoading(false);
        });

        fetchDataTask.setOnFailed(event -> {
            mainView.getStatusLabel().textProperty().unbind();
            mainView.getStatusLabel().setText("Gagal mengambil data: " + fetchDataTask.getException().getMessage());
            mainView.showLoading(false);
        });
        
        mainView.getStatusLabel().textProperty().bind(fetchDataTask.messageProperty());
        mainView.showLoading(true);
        
        new Thread(fetchDataTask).start();
    }

    private void handleDownloadChart() {
        if (mainView.getLineChart().getData().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Tidak ada data pada chart untuk disimpan.", ButtonType.OK).showAndWait();
            return;
        }
        
        try {
            String cryptoId = mainView.getSelectedCrypto();
            String days = mainView.getSelectedTimeRange();
            String currency = mainView.getSelectedCurrency();
            
            File savedFile = imageExporter.saveAsPng(primaryStage, mainView.getLineChart(), cryptoId, days, currency);
            
            if (savedFile != null) {
                new Alert(Alert.AlertType.INFORMATION, "Chart berhasil disimpan sebagai:\n" + savedFile.getAbsolutePath(), ButtonType.OK).showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Gagal menyimpan chart: " + e.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}