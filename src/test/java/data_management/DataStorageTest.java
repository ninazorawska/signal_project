package data_management;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DataStorageTest {
    private DataStorage dataStorage;

    @BeforeEach
    void setUp() {
        dataStorage = DataStorage.getInstance();
    }

    @Test
    void testAddPatientData() {
        dataStorage.addPatientData(1, 100.0, "HeartRate", 1714376789050L);
        dataStorage.addPatientData(1, 120.0, "BloodPressure", 1714376789051L);
        dataStorage.addPatientData(2, 130.0, "HeartRate", 1714376789052L);

        List<PatientRecord> records1 = dataStorage.getRecords(1, 1714376789040L, 1714376789060L);
        List<PatientRecord> records2 = dataStorage.getRecords(2, 1714376789040L, 1714376789060L);

        assertEquals(2, records1.size());
        assertEquals(1, records2.size());
    }



    @Test
    void testGetRecordsWithNoPatient() {
        List<PatientRecord> records = dataStorage.getRecords(1, 1714376789040L, 1714376789060L);
        assertTrue(records.isEmpty());
    }

    @Test
    void testGetAllPatients() {
        dataStorage.addPatientData(1, 100.0, "HeartRate", 1714376789050L);
        dataStorage.addPatientData(2, 130.0, "HeartRate", 1714376789052L);

        List<Patient> patients = dataStorage.getAllPatients();
        assertEquals(2, patients.size());
    }

    @Test
    void testAddPatientDataWithExistingPatient() {
        dataStorage.addPatientData(1, 100.0, "HeartRate", 1714376789050L);
        dataStorage.addPatientData(1, 110.0, "HeartRate", 1714376789051L);

        List<PatientRecord> records = dataStorage.getRecords(1, 1714376789040L, 1714376789060L);
        assertEquals(2, records.size());
    }

    @Test
    void testGetRecordsWithNoData() {
        dataStorage.addPatientData(1, 100.0, "HeartRate", 1714376789050L);
        List<PatientRecord> records = dataStorage.getRecords(2, 1714376789040L, 1714376789060L);
        assertTrue(records.isEmpty());
    }

    @Test
    void testGetRecordsWithOverlappingTimeRanges() {
        dataStorage.addPatientData(1, 100.0, "HeartRate", 1714376789050L);
        dataStorage.addPatientData(1, 120.0, "BloodPressure", 1714376789055L);
        dataStorage.addPatientData(1, 130.0, "HeartRate", 1714376789060L);

        List<PatientRecord> records = dataStorage.getRecords(1, 1714376789050L, 1714376789060L);
        assertEquals(3, records.size());
    }

    @Test
    void testAddAndRetrieveMultiplePatients() {
        dataStorage.addPatientData(1, 100.0, "HeartRate", 1714376789050L);
        dataStorage.addPatientData(2, 120.0, "BloodPressure", 1714376789051L);
        dataStorage.addPatientData(3, 130.0, "HeartRate", 1714376789052L);
        dataStorage.addPatientData(4, 140.0, "BloodPressure", 1714376789053L);

        List<Patient> patients = dataStorage.getAllPatients();
        assertEquals(4, patients.size());
    }

    @Test
    void testGetRecordsWithExactTimeMatch() {
        dataStorage.addPatientData(1, 100.0, "HeartRate", 1714376789050L);
        dataStorage.addPatientData(1, 120.0, "BloodPressure", 1714376789051L);

        List<PatientRecord> records = dataStorage.getRecords(1, 1714376789050L, 1714376789051L);
        assertEquals(2, records.size());
    }

    @Test
    void testGetRecordsWithFutureTimeRange() {
        dataStorage.addPatientData(1, 100.0, "HeartRate", 1714376789050L);

        List<PatientRecord> records = dataStorage.getRecords(1, 1714376789060L, 1714376789070L);
        assertTrue(records.isEmpty());
    }

    @Test
    void testGetRecordsWithPastTimeRange() {
        dataStorage.addPatientData(1, 100.0, "HeartRate", 1714376789050L);

        List<PatientRecord> records = dataStorage.getRecords(1, 1714376789040L, 1714376789049L);
        assertTrue(records.isEmpty());
    }

    @Test
    void testConcurrency() throws InterruptedException {
        Runnable addTask = () -> {
            for (int i = 0; i < 100; i++) {
                dataStorage.addPatientData(1, 100.0 + i, "HeartRate", 1714376789050L + i);
            }
        };

        Thread thread1 = new Thread(addTask);
        Thread thread2 = new Thread(addTask);
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        List<PatientRecord> records = dataStorage.getRecords(1, 1714376789050L, 1714376789150L);
        assertEquals(200, records.size());
        }

    @Test
    void testAddPatientDataWithNegativeValues() {
        dataStorage.addPatientData(1, -100.0, "HeartRate", 1714376789050L);
        dataStorage.addPatientData(1, -120.0, "BloodPressure", 1714376789051L);

        List<PatientRecord> records = dataStorage.getRecords(1, 1714376789050L, 1714376789060L);
        assertEquals(2, records.size());
        assertEquals(-100.0, records.get(0).getMeasurementValue());
        assertEquals(-120.0, records.get(1).getMeasurementValue());
    }

    @Test
    void testGetRecordsWithZeroTimestamp() {
        dataStorage.addPatientData(1, 100.0, "HeartRate", 0L);

        List<PatientRecord> records = dataStorage.getRecords(1, 0L, 1L);
        assertEquals(1, records.size());
        assertEquals(100.0, records.get(0).getMeasurementValue());
    }

    @Test
    void testAddPatientDataWithDuplicateRecords() {
        dataStorage.addPatientData(1, 100.0, "HeartRate", 1714376789050L);
        dataStorage.addPatientData(1, 100.0, "HeartRate", 1714376789051L);

        List<PatientRecord> records = dataStorage.getRecords(1, 1714376789040L, 1714376789060L);
        assertEquals(2, records.size());
    }


    @Test
    void testAddPatientDataWithNullValues() {
        assertThrows(NullPointerException.class, () -> {
            dataStorage.addPatientData(1, 100.0, null, 1714376789050L);
        });
    }
}
