package data_management;

import com.alerts.Alert;
import com.alerts.AlertGenerator;
import com.data_management.CustomWebSocketClient;
import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import org.java_websocket.client.WebSocketClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class IntegrationTest {
    private DataStorage dataStorage;
    private CustomWebSocketClient client;
    private AlertGenerator alertGenerator;

    @BeforeEach
    void setUp() throws URISyntaxException {
        dataStorage = new DataStorage(null);
        client = new CustomWebSocketClient("ws://localhost:8080", dataStorage);
        alertGenerator = new AlertGenerator(dataStorage);
    }

    @Test
    void testIntegration() {
        String message = "1,1627842123000,HeartRate,78.0";
        client.onMessage(message);

        List<PatientRecord> records = dataStorage.getRecords(1, 1627842123000L, 1627842124000L);
        assertEquals(1, records.size());
        assertEquals(78.0, records.get(0).getMeasurementValue());

        Patient patient = dataStorage.getAllPatients().get(0);
        alertGenerator.evaluateData(patient);
        List<Alert> alerts = alertGenerator.getAlerts();
        assertTrue(alerts.isEmpty()); // Assuming 78.0 HeartRate does not trigger an alert
    }
}
