package com.strategies;

public class ECGStrategy implements AlertStrategy {
    @Override
    public void checkAlert() {
        // Logic to check blood pressure alert
        System.out.println("Checking for ECG anomalies...");
    }
}
