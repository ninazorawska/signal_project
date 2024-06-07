package data_management;

import org.java_websocket.client.WebSocketClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.data_management.DataStorage;
import com.data_management.WebSocketDataReader;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.mockito.Mockito.*;

class WebSocketDataReaderTest {
    private DataStorage mockDataStorage;
    private WebSocketDataReader dataReader;

    @BeforeEach
    void setUp() throws URISyntaxException {
        mockDataStorage = mock(DataStorage.class);
        dataReader = new WebSocketDataReader("ws://localhost:8080", mockDataStorage);
    }

    @Test
    void testReadData() throws IOException {
        dataReader.readData(mockDataStorage);
        // Verify if the WebSocket client connects and starts receiving data
    }

    @Test
    void testProcessMessage() {
        String message = "1,1627842123000,HeartRate,78.0";
        dataReader.processMessage(message);
        verify(mockDataStorage).addPatientData(1, 78.0, "HeartRate", 1627842123000L);
    }

    @Test
    void testProcessInvalidMessage() {
        String message = "invalid,message";
        dataReader.processMessage(message);
        verify(mockDataStorage, never()).addPatientData(anyInt(), anyDouble(), anyString(), anyLong());
    }

    @Test
    void testProcessMalformedMessage() {
        String message = "1,notatimestamp,HeartRate,78.0";
        dataReader.processMessage(message);
        verify(mockDataStorage, never()).addPatientData(anyInt(), anyDouble(), anyString(), anyLong());
    }
}
