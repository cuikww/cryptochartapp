package com.my_app;

import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MainView extends VBox {

    private final ComboBox<String> cryptoComboBox;
    private final ComboBox<String> timeRangeComboBox;
    private final ComboBox<String> currencyComboBox;
    private final Button fetchButton;
    private final Button downloadButton;
    private final ProgressIndicator progressIndicator;
    private final LineChart<String, Number> lineChart;
    private final Label statusLabel;

    public MainView() {
        cryptoComboBox = new ComboBox<>();

        timeRangeComboBox = new ComboBox<>();
        timeRangeComboBox.getItems().addAll("7", "14", "30", "90");
        timeRangeComboBox.setValue("14");

        currencyComboBox = new ComboBox<>();
        currencyComboBox.getItems().addAll("usd", "idr", "eur");
        currencyComboBox.setValue("usd");

        fetchButton = new Button("Ambil Data");
        downloadButton = new Button("Download Chart");
        progressIndicator = new ProgressIndicator(-1.0);
        progressIndicator.setVisible(false);

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Tanggal");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Harga (USD)");

        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Harga Kripto (14 Hari Terakhir)");
        lineChart.setAnimated(false);
        lineChart.setLegendVisible(false);

        statusLabel = new Label("Pilih kripto dan klik 'Ambil Data'");

        HBox topControls = new HBox(10);
        topControls.setAlignment(Pos.CENTER);
        topControls.getChildren().addAll(
            new Label("Pilih Kripto:"), cryptoComboBox,
            new Label("Rentang Waktu (Hari):"), timeRangeComboBox,
            new Label("Mata Uang:"), currencyComboBox,
            fetchButton, progressIndicator
        );

        VBox bottomVBox = new VBox(15);
        bottomVBox.setAlignment(Pos.CENTER);

        HBox downloadHBox = new HBox();
        downloadHBox.setAlignment(Pos.CENTER);
        downloadHBox.getChildren().add(downloadButton);

        bottomVBox.getChildren().addAll(downloadHBox, statusLabel);

        this.setPadding(new Insets(10));
        this.setSpacing(10);
        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(topControls, lineChart, bottomVBox);
    }

    public String getSelectedCrypto() {
        return cryptoComboBox.getValue();
    }

    public String getSelectedTimeRange() {
        return timeRangeComboBox.getValue();
    }

    public String getSelectedCurrency() {
        return currencyComboBox.getValue();
    }

    public void updateYAxisLabel(String currency) {
        String upperCurrency = currency.toUpperCase();
        ((NumberAxis) lineChart.getYAxis()).setLabel("Harga (" + upperCurrency + ")");
    }

    public void updateChart(XYChart.Series<String, Number> series) {
        lineChart.getData().clear();
        lineChart.getData().add(series);
    }
    
    public void showLoading(boolean isLoading) {
        progressIndicator.setVisible(isLoading);
        fetchButton.setDisable(isLoading);
    }

    public void setCryptoItems(List<String> items) {
        cryptoComboBox.getItems().clear();
        cryptoComboBox.getItems().addAll(items);
        if (!items.isEmpty() && cryptoComboBox.getValue() == null) {
            cryptoComboBox.setValue(items.get(0));
        }
    }

    public Button getFetchButton() { return fetchButton; }
    public Button getDownloadButton() { return downloadButton; }
    public Label getStatusLabel() { return statusLabel; }
    public LineChart<String, Number> getLineChart() { return lineChart; }
}