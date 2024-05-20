package data_management;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.data_management.DataStorage;
import com.data_management.FileDataReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileDataReaderTest {
    private DataStorage mockDataStorage;
    private FileDataReader fileDataReader;

    @BeforeEach
    void setUp() {
        mockDataStorage = mock(DataStorage.class);
        fileDataReader = new FileDataReader();
    }

    @Test
    void testReadDataWithValidDirectory() throws IOException {
        Path tempDir = Files.createTempDirectory("tempDir");
        Path tempFile = Files.createFile(tempDir.resolve("data.csv"));
        Files.write(tempFile, List.of("1,100.0,HeartRate,1714376789050", "2,120.0,BloodPressure,1714376789051"));

        fileDataReader.readData(tempDir, mockDataStorage);

        verify(mockDataStorage, times(1)).addPatientData(1, 100.0, "HeartRate", 1714376789050L);
        verify(mockDataStorage, times(1)).addPatientData(2, 120.0, "BloodPressure", 1714376789051L);

        Files.deleteIfExists(tempFile);
        Files.deleteIfExists(tempDir);
    }

    @Test
    void testReadDataWithInvalidDirectory() {
        Path invalidDir = Paths.get("invalidDir");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            fileDataReader.readData(invalidDir, mockDataStorage);
        });

        assertEquals("Specified path is not a directory: invalidDir", exception.getMessage());
    }

    @Test
    void testParseAndStoreDataWithMalformedData() throws IOException {
        Path tempFile = Files.createTempFile("data", ".csv");
        Files.write(tempFile, List.of("1,100.0,HeartRate", "2,120.0,BloodPressure,1714376789051"));

        fileDataReader.parseAndStoreData(tempFile, mockDataStorage);

        verify(mockDataStorage, never()).addPatientData(eq(1), anyDouble(), anyString(), anyLong());
        verify(mockDataStorage, times(1)).addPatientData(2, 120.0, "BloodPressure", 1714376789051L);

        Files.deleteIfExists(tempFile);
    }

    @Test
    void testParseAndStoreDataWithValidData() throws IOException {
        Path tempFile = Files.createTempFile("data", ".csv");
        Files.write(tempFile, List.of("1,100.0,HeartRate,1714376789050", "2,120.0,BloodPressure,1714376789051"));

        fileDataReader.parseAndStoreData(tempFile, mockDataStorage);

        verify(mockDataStorage, times(1)).addPatientData(1, 100.0, "HeartRate", 1714376789050L);
  
    }
}