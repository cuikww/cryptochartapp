package com.my_app;

import javafx.geometry.Insets;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MainView extends VBox {

    private final ComboBox<String> cryptoComboBox;
    private final Button fetchButton;
    private final Button downloadButton;
    private final ProgressIndicator progressIndicator;
    private final LineChart<String, Number> lineChart;
    private final Label statusLabel;

    public MainView() {
        // Inisialisasi komponen UI
        cryptoComboBox = new ComboBox<>();
        cryptoComboBox.getItems().addAll("bitcoin", "ethereum", "dogecoin", "solana");
        cryptoComboBox.setValue("bitcoin");

        fetchButton = new Button("Ambil Data");
        downloadButton = new Button("Download Chart");
        progressIndicator = new ProgressIndicator(-1.0);
        progressIndicator.setVisible(false);

        // Inisialisasi grafik
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Tanggal");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Harga (USD)");
        
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Harga Kripto (14 Hari Terakhir)");
        lineChart.setAnimated(false);

        statusLabel = new Label("Pilih kripto dan klik 'Ambil Data'");

        // Susun layout
        HBox controls = new HBox(10, new Label("Pilih Kripto:"), cryptoComboBox, fetchButton, downloadButton, progressIndicator);
        this.setPadding(new Insets(10));
        this.setSpacing(10);
        this.getChildren().addAll(controls, lineChart, statusLabel);
    }

    // Metode publik untuk dikontrol oleh kelas lain
    public String getSelectedCrypto() {
        return cryptoComboBox.getValue();
    }

    public void updateChart(XYChart.Series<String, Number> series) {
        lineChart.getData().clear();
        lineChart.getData().add(series);
    }
    
    public void showLoading(boolean isLoading) {
        progressIndicator.setVisible(isLoading);
        fetchButton.setDisable(isLoading);
    }

    // Getter untuk komponen yang membutuhkan interaksi
    public Button getFetchButton() { return fetchButton; }
    public Button getDownloadButton() { return downloadButton; }
    public Label getStatusLabel() { return statusLabel; }
    public LineChart<String, Number> getLineChart() { return lineChart; }
}