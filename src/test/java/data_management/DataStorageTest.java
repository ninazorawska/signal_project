package data_management;

import org.junit.jupiter.api.Test;

import com.data_management.DataReader;
import com.data_management.DataStorage;
import com.data_management.FileDataReader;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DataStorageTest {

    @Test
    void testAddAndGetRecords() {
        DataReader reader = new FileDataReader(); // Replace with a mock if necessary
        DataStorage storage = new DataStorage(reader);

        storage.addPatientData(1, 100.0, "WhiteBloodCells", 1714376789050L);
        storage.addPatientData(1, 200.0, "WhiteBloodCells", 1714376789051L);

        List<PatientRecord> records = storage.getRecords(1, 1714376789050L, 1714376789051L);
        assertEquals(2, records.size());
        assertEquals(100.0, records.get(0).getMeasurementValue());
        assertEquals(200.0, records.get(1).getMeasurementValue());
    }

    @Test
    void testGetRecordsNoData() {
        DataReader reader = new FileDataReader(); // Replace with a mock if necessary
        DataStorage storage = new DataStorage(reader);

        List<PatientRecord> records = storage.getRecords(1, 1714376789050L, 1714376789051L);
        assertTrue(records.isEmpty());
    }

    @Test
    void testGetAllPatients() {
        DataReader reader = new FileDataReader(); // Replace with a mock if necessary
        DataStorage storage = new DataStorage(reader);

        storage.addPatientData(1, 100.0, "WhiteBloodCells", 1714376789050L);
        storage.addPatientData(2, 200.0, "WhiteBloodCells", 1714376789051L);

        List<Patient> patients = storage.getAllPatients();
        assertEquals(2, patients.size());
    }
}
