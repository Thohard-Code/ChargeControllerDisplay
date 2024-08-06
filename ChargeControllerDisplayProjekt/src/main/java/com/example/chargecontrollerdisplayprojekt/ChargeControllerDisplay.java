package com.example.chargecontrollerdisplayprojekt;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ChargeControllerDisplay extends Application {

    private XYChart.Series<Number, Number> series;
    private double batteryVoltage = 6; // Startspænding for batteriet
    private final double MAX_BATTERY_VOLTAGE = 12; // Maksimal batterispænding
    private boolean isCharging = true; // Angiver om batteriet oplades

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Charge Controller Display");

        // Labels for displaying voltages
        Label solarPanelVoltageLabel = new Label("Solar Panel Voltage: 0V");
        Label batteryVoltageLabel = new Label("Battery Voltage: 0V");

        // Setup the x and y axes for the graph
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Time");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Voltage");

        // Setup the line chart
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Voltage Over Time");

        series = new XYChart.Series<>();
        series.setName("Voltage Data");
        lineChart.getData().add(series);

        // Layout
        VBox vbox = new VBox();
        vbox.getChildren().addAll(solarPanelVoltageLabel, batteryVoltageLabel, lineChart);

        Scene scene = new Scene(vbox, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Simulate voltage updates
        simulateVoltageUpdates(solarPanelVoltageLabel, batteryVoltageLabel);
    }

    private void simulateVoltageUpdates(Label solarPanelVoltageLabel, Label batteryVoltageLabel) {
        new Thread(() -> {
            int time = 0;
            double solarPanelVoltage;

            while (true) {
                try {
                    Thread.sleep(1000); // Update every second
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Simulate solar panel voltage (random value between 10 and 18)
                solarPanelVoltage = 10 + Math.random() * 8;

                // Check if battery is fully charged
                if (batteryVoltage >= MAX_BATTERY_VOLTAGE) {
                    isCharging = false;
                } else if (batteryVoltage < MAX_BATTERY_VOLTAGE) {
                    isCharging = true;
                }

                // Simulate battery charging or discharging
                if (isCharging) {
                    double chargingRate = (solarPanelVoltage - 10) / 80; // Scale charging rate based on solar panel voltage
                    batteryVoltage += chargingRate;
                    if (batteryVoltage > MAX_BATTERY_VOLTAGE) {
                        batteryVoltage = MAX_BATTERY_VOLTAGE;
                    }
                } else {
                    batteryVoltage -= 0.05; // Discharge battery slowly
                    if (batteryVoltage < 10) {
                        batteryVoltage = 10; // Prevent battery from discharging below 10V
                    }
                }

                final int finalTime = time;
                final double finalSolarPanelVoltage = solarPanelVoltage;
                final double finalBatteryVoltage = batteryVoltage;

                javafx.application.Platform.runLater(() -> {
                    solarPanelVoltageLabel.setText(String.format("Solar Panel Voltage: %.2fV", finalSolarPanelVoltage));
                    batteryVoltageLabel.setText(String.format("Battery Voltage: %.2fV", finalBatteryVoltage));
                    series.getData().add(new XYChart.Data<>(finalTime, finalBatteryVoltage));
                });

                time++;
            }
        }).start();
    }
}