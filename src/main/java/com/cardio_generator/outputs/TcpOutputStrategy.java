package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

/**
 * An output strategy that sends data through TCP (Transmission Control Protocol) to a client.
 */

public class TcpOutputStrategy implements OutputStrategy {

    /** 
     * The server socket used for listening to incoming connections from clients.
    */
    
    private ServerSocket serverSocket;

    /**
     *  The client socket representing the connection to a specific client.
     */

    private Socket clientSocket;

    /**
     *  The output stream used for sending data to the connected client.
     */

    private PrintWriter out;

     /**
     * Constructs a new TcpOutputStrategy with a port as input.
     *
     * @param port, Integer representing the port number where the TCP server is waiting for connections.
     */

    public TcpOutputStrategy(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("TCP Server started on port " + port);

            // Accept clients in a new thread to not block the main thread
            Executors.newSingleThreadExecutor().submit(() -> {
                try {
                    clientSocket = serverSocket.accept();
                    out = new PrintWriter(clientSocket.getOutputStream(), true);
                    System.out.println("Client connected: " + clientSocket.getInetAddress());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
    * Sends the inputed data to the specified TCP connection.
    *
    * @param patientId, Integer representing the ID of the patient.
    * @param timestamp, Long associated with the timestamp.
    * @param label, String representing the label of the type of health data.
    * @param data, String representing the actual health data recorded for the patient.
    */

    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        if (out != null) {
            String message = String.format("%d,%d,%s,%s", patientId, timestamp, label, data);
            out.println(message);
        }
    }
}
