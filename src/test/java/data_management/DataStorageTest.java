package data_management;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DataStorageTest {
    private DataStorage dataStorage;

    @BeforeEach
    void setUp() {
        dataStorage = new DataStorage(null);
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
}
