package com.strategies;

public class HeartRateStrategy implements AlertStrategy {
    @Override
    public void checkAlert() {
        // Logic to check heart rate alert
        System.out.println("Monitoring for abnormal heart rates...");
    }
}

