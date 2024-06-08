package com.alerts;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import com.strategies.AlertStrategy;
import com.strategies.AlertStrategyFactory;

import java.util.ArrayList;
import java.util.List;

public class AlertGenerator {
    private final DataStorage dataStorage;
    private final List<AlertInterface> alerts;

    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        this.alerts = new ArrayList<>();
    }

    public void evaluateData(Patient patient) {
        for (PatientRecord record : patient.getRecords(0, System.currentTimeMillis())) {
            AlertStrategy strategy = AlertStrategyFactory.getStrategy(record.getRecordType());
            if (strategy != null) {
                AlertInterface alert = strategy.checkAlert(record);
                if (alert != null) {
                    alert = new PriorityAlertDecorator(alert);
                    alert = new RepeatedAlertDecorator(alert, 3);
                    alerts.add(alert);
                }
            }
        }
    }

    public List<AlertInterface> getAlerts() {
        return alerts;
    }
}
