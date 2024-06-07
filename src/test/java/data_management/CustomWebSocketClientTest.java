package data_management;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.data_management.CustomWebSocketClient;
import com.data_management.DataStorage;

import java.net.URISyntaxException;

import static org.mockito.Mockito.*;

class CustomWebSocketClientTest {
    private DataStorage mockDataStorage;
    private CustomWebSocketClient client;

    @BeforeEach
    void setUp() throws URISyntaxException {
        mockDataStorage = mock(DataStorage.class);
        client = new CustomWebSocketClient("ws://localhost:8080", mockDataStorage);
    }

    @Test
    void testOnMessageValidData() {
        String message = "1,1627842123000,HeartRate,78.0";
        client.onMessage(message);
        verify(mockDataStorage).addPatientData(1, 78.0, "HeartRate", 1627842123000L);
    }

    @Test
    void testOnMessageInvalidData() {
        String message = "invalid,message";
        client.onMessage(message);
        verify(mockDataStorage, never()).addPatientData(anyInt(), anyDouble(), anyString(), anyLong());
    }

    @Test
    void testOnMessageMalformedData() {
        String message = "1,notatimestamp,HeartRate,78.0";
        client.onMessage(message);
        verify(mockDataStorage, never()).addPatientData(anyInt(), anyDouble(), anyString(), anyLong());
    }

    @Test
    void testOnOpen() {
        ServerHandshake handshake = mock(ServerHandshake.class);
        client.onOpen(handshake);
        // Check for any initialization or logging if required
    }

    @Test
    void testOnClose() {
        client.onClose(1000, "Normal closure", true);
        // Verify any cleanup or logging if necessary
    }

    @Test
    void testOnError() {
        Exception ex = new Exception("Test exception");
        client.onError(ex);
        // Verify logging or error handling if needed
    }
}
