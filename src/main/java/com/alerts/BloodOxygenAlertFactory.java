package com.alerts;

public class BloodOxygenAlertFactory implements AlertFactory {
    @Override
    public AlertInterface createAlert(int patientId, String condition, long timestamp) {
        return new Alert(patientId, condition, timestamp, "High");
    }
}
