package com.my_app;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    private MainView mainView;
    private final CoinGeckoApiService apiService = new CoinGeckoApiService();
    private final ChartDataParser dataParser = new ChartDataParser();
    private final ChartImageExporter imageExporter = new ChartImageExporter();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Crypto Price Chart");

        mainView = new MainView();

        // Menghubungkan aksi tombol dengan logika
        mainView.getFetchButton().setOnAction(e -> handleFetchData());
        mainView.getDownloadButton().setOnAction(e -> handleDownloadChart());

        Scene scene = new Scene(mainView, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleFetchData() {
        String cryptoId = mainView.getSelectedCrypto();
        
        Task<XYChart.Series<String, Number>> fetchDataTask = new Task<>() {
            @Override
            protected XYChart.Series<String, Number> call() throws Exception {
                updateMessage("Mengambil data untuk " + cryptoId + "...");
                String jsonResponse = apiService.fetchPriceHistory(cryptoId);
                return dataParser.parsePriceHistory(jsonResponse, cryptoId);
            }
        };

        fetchDataTask.setOnSucceeded(event -> {
            mainView.getStatusLabel().textProperty().unbind();
            mainView.updateChart(fetchDataTask.getValue());
            mainView.getStatusLabel().setText("Data untuk '" + cryptoId + "' berhasil ditampilkan.");
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
            File savedFile = imageExporter.saveAsPng(mainView.getLineChart(), mainView.getSelectedCrypto());
            new Alert(Alert.AlertType.INFORMATION, "Chart berhasil disimpan sebagai:\n" + savedFile.getAbsolutePath(), ButtonType.OK).showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Gagal menyimpan chart: " + e.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}