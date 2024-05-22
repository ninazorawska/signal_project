package com.data_management;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.io.IOException;

public class WebSocketDataReader implements DataReader {

    private WebSocketClient webSocketClient;
    private DataStorage dataStorage;

    // Constructor to initialize WebSocketDataReader with server URI and DataStorage
    public WebSocketDataReader(String serverUri, DataStorage dataStorage) throws URISyntaxException {
        this.dataStorage = dataStorage;

        // Initialize WebSocketClient with the provided server URI
        this.webSocketClient = new WebSocketClient(new URI(serverUri)) {
            // Callback method invoked when the WebSocket connection is established
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                System.out.println("Connected to WebSocket server");
            }
            // Callback method invoked when a message is received from the WebSocket server
            @Override
            public void onMessage(String message) {
                processMessage(message);
            }
            // Callback method invoked when the WebSocket connection is closed
            @Override
            public void onClose(int code, String reason, boolean remote) {
                System.out.println("Disconnected from WebSocket server");
            }
            // Callback method invoked when an error occurs in the WebSocket connection
            @Override
            public void onError(Exception ex) {
                ex.printStackTrace();
            }
        };
    }

    // Method to start reading data from the WebSocket server
    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        this.webSocketClient.connect();
    }

    // Method to process the message received from the WebSocket server
    private void processMessage(String message) {
        // Assuming the message format is: patientId,timestamp,label,data
        String[] parts = message.split(",");
        int patientId = Integer.parseInt(parts[0]);
        double masurementvValue = Double.parseDouble(parts[1]);
        String recordType = parts[2];
        Long timeStamp = Long.parseLong(parts[3]);

        // Process the data and store it in the DataStorage
        dataStorage.addPatientData(patientId, masurementvValue, recordType, timeStamp);
    }
}
