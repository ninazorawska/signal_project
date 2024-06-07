package com.alerts;

// Import the Alert class from the provided package
import com.alerts.Alert;

public abstract class AlertFactory {
    public abstract Alert createAlert(String patientId, String condition, long timestamp);
}


