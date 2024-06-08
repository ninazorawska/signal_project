package com.strategies;

import com.alerts.Alert;
import com.data_management.PatientRecord;

public interface AlertStrategy {
    Alert checkAlert(PatientRecord record);
}
