package data_management;

import org.junit.jupiter.api.Test;

import com.data_management.PatientRecord;

import static org.junit.jupiter.api.Assertions.*;

class PatientRecordTest {

    @Test
    void testPatientRecordInitialization() {
        long now = System.currentTimeMillis();
        PatientRecord record = new PatientRecord(1, 100.0, "HeartRate", now);

        assertEquals(1, record.getPatientId());
        assertEquals(100.0, record.getMeasurementValue());
        assertEquals("HeartRate", record.getRecordType());
        assertEquals(now, record.getTimestamp());
    }
}
