package com.strategies;

public class OxygenSaturationStrategy implements AlertStrategy {
    @Override
    public void checkAlert() {
        // Logic to check oxygen saturation alert
        System.out.println("Observing oxygen levels for critical drops...");
    }
}