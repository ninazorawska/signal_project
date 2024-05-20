package com.alerts;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;

public class AlertGenerator {
    private DataStorage dataStorage;
    private List<Alert> alerts;

    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        this.alerts = new ArrayList<>();
    }

    /**
     * Evaluates patient data to check for conditions that may trigger alerts.
     * @param patient The patient whose data needs to be evaluated.
     */
    public void evaluateData(Patient patient) {
        List<PatientRecord> records = patient.getRecords(0, System.currentTimeMillis());

        for (PatientRecord record : records) {
            System.out.println("Evaluating record: " + record.getRecordType() + " with value: " + record.getMeasurementValue());
            
            if ("HeartRate".equals(record.getRecordType()) && record.getMeasurementValue() > 100) {
                triggerAlert(patient.getPatientId(), "High Heart Rate", record.getTimestamp(), "Heart rate exceeds 100 bpm.");
            }
            if ("BloodPressure".equals(record.getRecordType()) && record.getMeasurementValue() < 80) {
                triggerAlert(patient.getPatientId(), "Low Blood Pressure", record.getTimestamp(), "Blood pressure below 80 mmHg.");
            }
            if ("BloodPressure".equals(record.getRecordType()) && record.getMeasurementValue() > 140) {
                triggerAlert(patient.getPatientId(), "High Blood Pressure", record.getTimestamp(), "Blood pressure exceeds 140 mmHg.");
            }
            if ("Saturation".equals(record.getRecordType()) && record.getMeasurementValue() < 90) {
                triggerAlert(patient.getPatientId(), "Low Blood Saturation", record.getTimestamp(), "Blood saturation below 90%.");
            }
            if ("ECG".equals(record.getRecordType()) && record.getMeasurementValue() > 1.5) {
                triggerAlert(patient.getPatientId(), "Abnormal ECG", record.getTimestamp(), "ECG value exceeds 1.5 mV.");
            }
        }
    }

    /**
     * Triggers an alert if a specified condition is met.
     * @param patientId The ID of the patient for whom the alert is being triggered.
     * @param condition The condition that triggered the alert.
     * @param timestamp The time at which the alert was triggered.
     * @param message A message describing the alert condition.
     */
    private void triggerAlert(int patientId, String condition, long timestamp, String message) {
        Alert alert = new Alert(String.valueOf(patientId), condition, timestamp);
        alerts.add(alert);
        System.out.println("Alert triggered: " + message);
    }

    /**
     * Retrieves the list of alerts.
     * @return A list of alerts.
     */
    public List<Alert> getAlerts() {
        return new ArrayList<>(alerts);
    }
}
