package com.strategies;

import java.util.HashMap;
import java.util.Map;

public class AlertStrategyFactory {
    private static final Map<String, AlertStrategy> strategies = new HashMap<>();

    static {
        strategies.put("BloodPressure", new BloodPressureStrategy());
        strategies.put("HeartRate", new HeartRateStrategy());
        strategies.put("OxygenSaturation", new OxygenSaturationStrategy());
        strategies.put("ECG", new ECGStrategy());
    }

    public static AlertStrategy getStrategy(String recordType) {
        return strategies.get(recordType);
    }
}
