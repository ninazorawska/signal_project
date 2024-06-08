package com.alerts;

public interface AlertFactory {
    AlertInterface createAlert(int patientId, String condition, long timestamp);
}
