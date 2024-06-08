package com.strategies;

import com.alerts.Alert;
import com.data_management.PatientRecord;

public class BloodPressureStrategy implements AlertStrategy {

    @Override
    public Alert checkAlert(PatientRecord record) {
        if ("BloodPressure".equals(record.getRecordType())) {
            if (record.getMeasurementValue() > 140 || record.getMeasurementValue() < 90) {
                return new Alert(record.getPatientId(), "Critical Blood Pressure", record.getTimestamp(), "High");
            }
        }
        return null;
    }
}


