package com.cardio_generator;

import com.cardio_generator.outputs.*;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HealthDataSimulator {
    private static HealthDataSimulator instance;
    private ScheduledExecutorService scheduler;
    private OutputStrategy outputStrategy;
    private int patientCount;

    private HealthDataSimulator() {
        this.scheduler = Executors.newScheduledThreadPool(10);
        this.outputStrategy = new ConsoleOutputStrategy(); // Default output strategy
        this.patientCount = 50; // Default patient count
    }

    public static HealthDataSimulator getInstance() {
        if (instance == null) {
            instance = new HealthDataSimulator();
        }
        return instance;
    }

    public void parseArguments(String[] args) {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--output":
                    if (i + 1 < args.length) {
                        String output = args[++i];
                        if (output.startsWith("console")) {
                            outputStrategy = new ConsoleOutputStrategy();
                        } else if (output.startsWith("file:")) {
                            outputStrategy = new FileOutputStrategy(output.split(":")[1]);
                        } else if (output.startsWith("tcp:")) {
                            outputStrategy = new TcpOutputStrategy(Integer.parseInt(output.split(":")[1]));
                        } else if (output.startsWith("websocket:")) {
                            outputStrategy = new WebSocketOutputStrategy(Integer.parseInt(output.split(":")[1]));
                        }
                    }
                    break;
                case "--patient-count":
                    if (i + 1 < args.length) {
                        patientCount = Integer.parseInt(args[++i]);
                    }
                    break;
                case "-h":
                    printHelp();
                    System.exit(0);
            }
        }
    }

    private void printHelp() {
        System.out.println("Usage: java HealthDataSimulator [options]");
        System.out.println("Options:");
        System.out.println("  -h                       Show help and exit.");
        System.out.println("  --patient-count <count>  Specify the number of patients to simulate data for (default: 50).");
        System.out.println("  --output <type>          Define the output method. Options are:");
        System.out.println("                             'console' for console output,");
        System.out.println("                             'file:<directory>' for file output,");
        System.out.println("                             'websocket:<port>' for WebSocket output,");
        System.out.println("                             'tcp:<port>' for TCP socket output.");
        System.out.println("Example:");
        System.out.println("  java HealthDataSimulator --patient-count 100 --output websocket:8080");
        System.out.println("  This command simulates data for 100 patients and sends the output to WebSocket clients connected to port 8080.");
    }

    public void startSimulation() {
        // Generate and output data logic
        System.out.println("Starting simulation for " + patientCount + " patients.");
        // Here we can add the code to simulate patient data
    }

    private void scheduleTasksForPatients(List<Integer> patientIds) {
        for (Integer patientId : patientIds) {
            for (int i = 0; i < 5; i++) {
                scheduler.scheduleAtFixedRate(
                    () -> outputStrategy.output(patientId, System.currentTimeMillis(), "HeartRate", "78"),
                    0,
                    1,
                    TimeUnit.SECONDS
                );
            }
        }
    }
}
