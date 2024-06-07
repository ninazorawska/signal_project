package data_management;

import com.data_management.WebSocketDataReader;
import com.data_management.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class WebSocketDataReaderTest {
    private WebSocketDataReader dataReader;
    private DataStorage dataStorage;

    @BeforeEach
    void setUp() throws URISyntaxException {
        dataStorage = mock(DataStorage.class);
        dataReader = new WebSocketDataReader("ws://localhost:8080", dataStorage);
    }

    @Test
    void testProcessMessage() {
        String validMessage = "1,1627842123000,HeartRate,78.0";
        dataReader.processMessage(validMessage);
        verify(dataStorage, times(1)).addPatientData(1, 78.0, "HeartRate", 1627842123000L);
    }

    @Test
    void testProcessInvalidMessage() {
        String invalidMessage = "1,invalidtimestamp,HeartRate,78.0";
        dataReader.processMessage(invalidMessage);
        verify(dataStorage, never()).addPatientData(anyInt(), anyDouble(), anyString(), anyLong());
    }

    @Test
    void testProcessMalformedMessage() {
        String malformedMessage = "1,1627842123000,HeartRate";
        dataReader.processMessage(malformedMessage);
        verify(dataStorage, never()).addPatientData(anyInt(), anyDouble(), anyString(), anyLong());
    }

    @Test
    void testProcessMessageWithNonNumericData() {
        String nonNumericDataMessage = "1,1627842123000,HeartRate,nonNumericData";
        dataReader.processMessage(nonNumericDataMessage);
        verify(dataStorage, never()).addPatientData(anyInt(), anyDouble(), anyString(), anyLong());
    }
}
