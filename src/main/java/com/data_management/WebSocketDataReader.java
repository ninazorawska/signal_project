package com.data_management;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.io.IOException;

public class WebSocketDataReader implements DataReader {
    private WebSocketClient webSocketClient;
    private DataStorage dataStorage;

    public WebSocketDataReader(String serverUri, DataStorage dataStorage) throws URISyntaxException {
        this.dataStorage = dataStorage;

        this.webSocketClient = new WebSocketClient(new URI(serverUri)) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                System.out.println("Connected to WebSocket server");
            }

            @Override
            public void onMessage(String message) {
                processMessage(message);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                System.out.println("Disconnected from WebSocket server");
            }

            @Override
            public void onError(Exception ex) {
                ex.printStackTrace();
            }
        };
    }

    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        this.webSocketClient.connect();
    }

    public void processMessage(String message) {
        String[] parts = message.split(",");
        if (parts.length != 4) {
            System.err.println("Invalid message format: " + message);
            return;
        }

        try {
            int patientId = Integer.parseInt(parts[0]);
            double measurementValue = Double.parseDouble(parts[1]);
            String recordType = parts[2];
            long timeStamp = Long.parseLong(parts[3]);

            dataStorage.addPatientData(patientId, measurementValue, recordType, timeStamp);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing message: " + message);
            e.printStackTrace();
        }
    }
}
