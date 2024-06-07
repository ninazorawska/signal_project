package com.data_management;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Custom WebSocket client that connects to a WebSocket server and processes incoming messages.
 */
public class CustomWebSocketClient extends WebSocketClient {

    private final DataStorage dataStorage;

    /**
     * Constructor to initialize CustomWebSocketClient with server URI and DataStorage.
     *
     * @param serverUri   The URI of the WebSocket server to connect to.
     * @param dataStorage The DataStorage instance to store received data.
     * @throws URISyntaxException If the provided URI syntax is invalid.
     */
    public CustomWebSocketClient(String serverUri, DataStorage dataStorage) throws URISyntaxException {
        super(new URI(serverUri));
        this.dataStorage = dataStorage;
    }

    /**
     * Callback method invoked when the WebSocket connection is established.
     *
     * @param handshakedata Information about the handshake.
     */
    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to WebSocket server");
    }

    /**
     * Callback method invoked when a message is received from the WebSocket server.
     *
     * @param message The message received from the server.
     */
    @Override
    public void onMessage(String message) {
        try {
            processMessage(message);
        } catch (Exception e) {
            System.err.println("Error processing message: " + message);
            e.printStackTrace();
        }
    }

    /**
     * Callback method invoked when the WebSocket connection is closed.
     *
     * @param code   The closure code.
     * @param reason The reason for the closure.
     * @param remote Whether the closure was initiated by the remote host.
     */
    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Disconnected from WebSocket server: " + reason);
    }

    /**
     * Callback method invoked when an error occurs in the WebSocket connection.
     *
     * @param ex The exception that was thrown.
     */
    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

    /**
     * Method to process the message received from the WebSocket server.
     *
     * @param message The message received from the WebSocket server.
     */
    private void processMessage(String message) {
        // Assuming the message format is: patientId,timestamp,label,data
        String[] parts = message.split(",");
        if (parts.length != 4) {
            System.err.println("Invalid message format: " + message);
            return;
        }

        try {
            int patientId = Integer.parseInt(parts[0]);
            long timestamp = Long.parseLong(parts[1]);
            String recordType = parts[2];
            double measurementValue = Double.parseDouble(parts[3]);

            // Store the data in DataStorage
            dataStorage.addPatientData(patientId, measurementValue, recordType, timestamp);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing message: " + message);
            e.printStackTrace();
        }
    }
}
