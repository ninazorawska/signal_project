package data_management;

import com.alerts.AlertGenerator;
import com.data_management.DataStorage;
import com.data_management.Patient;
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
        patient.addRecord(101.0, "HeartRate", now); // Should trigger High Heart Rate alert
        patient.addRecord(75.0, "BloodPressure", now + 1000); // Should trigger Low Blood Pressure alert
        patient.addRecord(150.0, "BloodPressure", now + 2000); // Should trigger High Blood Pressure alert

        dataStorage.addPatientData(1, 101.0, "HeartRate", now);

    }
}
