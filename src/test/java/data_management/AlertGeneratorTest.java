package data_management;

import com.alerts.Alert;
import com.alerts.AlertGenerator;
import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AlertGeneratorTest {
    private DataStorage mockDataStorage;
    private AlertGenerator alertGenerator;

    @BeforeEach
    void setUp() {
        mockDataStorage = mock(DataStorage.class);
        alertGenerator = new AlertGenerator(mockDataStorage);
    }

    @Test
    void testEvaluateDataWithHighHeartRate() {
        Patient mockPatient = mock(Patient.class);
        List<PatientRecord> records = Arrays.asList(
                new PatientRecord(1, 101.0, "HeartRate", System.currentTimeMillis())
        );
        when(mockPatient.getRecords(anyLong(), anyLong())).thenReturn(records);

        alertGenerator.evaluateData(mockPatient);

        List<Alert> alerts = alertGenerator.getAlerts();
        assertEquals(1, alerts.size());
        assertEquals("High Heart Rate", alerts.get(0).getCondition());
    }

    @Test
    void testEvaluateDataWithNormalHeartRate() {
        Patient mockPatient = mock(Patient.class);
        List<PatientRecord> records = Arrays.asList(
                new PatientRecord(1, 80.0, "HeartRate", System.currentTimeMillis())
        );
        when(mockPatient.getRecords(anyLong(), anyLong())).thenReturn(records);

        alertGenerator.evaluateData(mockPatient);

        List<Alert> alerts = alertGenerator.getAlerts();
        assertTrue(alerts.isEmpty());
    }

    @Test
    void testEvaluateDataWithMultipleAlerts() {
        Patient mockPatient = mock(Patient.class);
        List<PatientRecord> records = Arrays.asList(
                new PatientRecord(1, 101.0, "HeartRate", System.currentTimeMillis()),
                new PatientRecord(1, 150.0, "BloodPressure", System.currentTimeMillis()),
                new PatientRecord(1, 85.0, "BloodPressure", System.currentTimeMillis())
        );
        when(mockPatient.getRecords(anyLong(), anyLong())).thenReturn(records);

        alertGenerator.evaluateData(mockPatient);

        List<Alert> alerts = alertGenerator.getAlerts();
        assertEquals(2, alerts.size());
        assertEquals("High Heart Rate", alerts.get(0).getCondition());
        assertEquals("High Blood Pressure", alerts.get(1).getCondition());
    }

    @Test
    void testEvaluateDataWithNoRecords() {
        Patient mockPatient = mock(Patient.class);
        when(mockPatient.getRecords(anyLong(), anyLong())).thenReturn(Arrays.asList());

        alertGenerator.evaluateData(mockPatient);

        List<Alert> alerts = alertGenerator.getAlerts();
        assertTrue(alerts.isEmpty());
    }

    @Test
    void testEvaluateDataWithLowBloodPressure() {
        Patient mockPatient = mock(Patient.class);
        List<PatientRecord> records = Arrays.asList(
                new PatientRecord(1, 70.0, "BloodPressure", System.currentTimeMillis())
        );
        when(mockPatient.getRecords(anyLong(), anyLong())).thenReturn(records);

        alertGenerator.evaluateData(mockPatient);

        List<Alert> alerts = alertGenerator.getAlerts();
        assertEquals(1, alerts.size());
        assertEquals("Low Blood Pressure", alerts.get(0).getCondition());
    }
}
