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
    private Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        mockDataStorage = mock(DataStorage.class);
        tempDir = Files.createTempDirectory("tempDir");
        fileDataReader = new FileDataReader(tempDir);
    }

    @Test
    void testReadDataWithValidDirectory() throws IOException {
        Path tempFile = Files.createFile(tempDir.resolve("data.csv"));
        Files.write(tempFile, List.of("1,100.0,HeartRate,1714376789050", "2,120.0,BloodPressure,1714376789051"));

        fileDataReader.readData(mockDataStorage);

        verify(mockDataStorage, times(1)).addPatientData(1, 100.0, "HeartRate", 1714376789050L);
        verify(mockDataStorage, times(1)).addPatientData(2, 120.0, "BloodPressure", 1714376789051L);

        Files.deleteIfExists(tempFile);
        Files.deleteIfExists(tempDir);
    }

    @Test
    void testReadDataWithInvalidDirectory() {
        Path invalidDir = Paths.get("invalidDir");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new FileDataReader(invalidDir);
        });

        assertEquals("Specified path is not a directory: invalidDir", exception.getMessage());
    }

    @Test
    void testParseAndStoreDataWithMalformedData() throws IOException {
        Path tempFile = Files.createTempFile(tempDir, "data", ".csv");
        Files.write(tempFile, List.of("1,100.0,HeartRate", "2,120.0,BloodPressure,1714376789051"));

        fileDataReader.parseAndStoreData(tempFile, mockDataStorage);

        verify(mockDataStorage, never()).addPatientData(eq(1), anyDouble(), anyString(), anyLong());
        verify(mockDataStorage, times(1)).addPatientData(2, 120.0, "BloodPressure", 1714376789051L);

        Files.deleteIfExists(tempFile);
    }

    @Test
    void testParseAndStoreDataWithValidData() throws IOException {
        Path tempFile = Files.createTempFile(tempDir, "data", ".csv");
        Files.write(tempFile, List.of("1,100.0,HeartRate,1714376789050", "2,120.0,BloodPressure,1714376789051"));

        fileDataReader.parseAndStoreData(tempFile, mockDataStorage);

        verify(mockDataStorage, times(1)).addPatientData(1, 100.0, "HeartRate", 1714376789050L);
        verify(mockDataStorage, times(1)).addPatientData(2, 120.0, "BloodPressure", 1714376789051L);

        Files.deleteIfExists(tempFile);
    }
}
