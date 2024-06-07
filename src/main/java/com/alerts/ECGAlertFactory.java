package com.alerts;

public class ECGAlertFactory extends AlertFactory {
    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        // Create a new Alert instance using the provided parameters
        return new Alert(patientId, condition, timestamp);
    }
}
