package data_management;

import com.data_management.Patient;
import com.data_management.PatientRecord;
import com.alerts.Alert;
import com.alerts.AlertGenerator;
import com.data_management.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AlertGeneratorTest {
    private DataStorage dataStorage;
    private AlertGenerator alertGenerator;
    private Patient patient;

    @BeforeEach
    void setUp() {
        dataStorage = new DataStorage(null);
        alertGenerator = new AlertGenerator(dataStorage);
        patient = new Patient(1);
    }

    @Test
    void testMultipleAlerts() {
        long now = System.currentTimeMillis();
        patient.addRecord(101.0, "HeartRate", now);
        patient.addRecord(75.0, "BloodPressure", now + 1000);
        patient.addRecord(150.0, "BloodPressure", now + 2000);
        
        dataStorage.addPatientData(1, 101.0, "HeartRate", now);
        dataStorage.addPatientData(1, 75.0, "BloodPressure", now + 1000);
        dataStorage.addPatientData(1, 150.0, "BloodPressure", now + 2000);

        alertGenerator.evaluateData(patient);
        List<Alert> alerts = alertGenerator.getAlerts();
        assertEquals(3, alerts.size());
    }
}
