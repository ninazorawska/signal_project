package data_management;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.data_management.CustomWebSocketClient;
import com.data_management.DataStorage;
import com.data_management.PatientRecord;
import com.cardio_generator.outputs.WebSocketOutputStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.util.List;

public class IntegrationTest {
    private DataStorage dataStorage;
    private WebSocketOutputStrategy outputStrategy;
    private CustomWebSocketClient client;

    @BeforeEach
    public void setUp() throws URISyntaxException {
        dataStorage = new DataStorage(null);
        outputStrategy = new WebSocketOutputStrategy(8080);
        client = new CustomWebSocketClient("ws://localhost:8080", dataStorage);
    }

    @Test
    public void testRealTimeDataProcessing() throws InterruptedException {
        client.connectBlocking();
        outputStrategy.output(1, 1627842123000L, "HeartRate", "78.0");

        // Wait for the message to be processed
        Thread.sleep(1000);

        List<PatientRecord> records = dataStorage.getRecords(1, 1627842123000L, 1627842123000L);
        assertEquals(1, records.size());
        assertEquals(78.0, records.get(0).getMeasurementValue());
    }
}
