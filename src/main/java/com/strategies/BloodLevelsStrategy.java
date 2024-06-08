package com.strategies;

import com.alerts.Alert;
import com.alerts.AlertInterface;
import com.data_management.PatientRecord;

public class BloodLevelsStrategy implements AlertStrategy {
    @Override
    public Alert checkAlert(PatientRecord record) {
        if (record.getMeasurementValue() < 90) {
            return new Alert(record.getPatientId(), "Low Blood Levels", record.getTimestamp(), "High");
        }
        return null;
    }
}
