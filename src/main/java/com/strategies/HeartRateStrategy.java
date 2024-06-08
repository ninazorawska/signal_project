package com.strategies;

import com.alerts.Alert;
import com.data_management.PatientRecord;

public class HeartRateStrategy implements AlertStrategy {

    @Override
    public Alert checkAlert(PatientRecord record) {
        if ("HeartRate".equals(record.getRecordType())) {
            if (record.getMeasurementValue() > 100 || record.getMeasurementValue() < 60) {
                return new Alert(record.getPatientId(), "Abnormal Heart Rate", record.getTimestamp(), null);
            }
        }
        return null;
    }
}

