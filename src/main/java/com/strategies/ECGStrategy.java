package com.strategies;

import com.alerts.Alert;
import com.data_management.PatientRecord;

public class ECGStrategy implements AlertStrategy {
    @Override
    public Alert checkAlert(PatientRecord record) {
        if ("ECG".equals(record.getRecordType())) {
            if (record.getMeasurementValue() < 0.5 || record.getMeasurementValue() > 2.0) {
                return new Alert(record.getPatientId(), "Abnormal ECG", record.getTimestamp(), "High");
            }
        }
        return null;
    }
}

