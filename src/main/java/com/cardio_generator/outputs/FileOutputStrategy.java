package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents an output strategy that writes data of patients to files
 * in a structured and organized way.
 */

public class FileOutputStrategy implements OutputStrategy {
     
    // Directory where the files will be stored
    private String baseDirectory; // Changed variable name to camelCase

    // Map to store file paths based on label 
    public final ConcurrentHashMap<String, String> file_map = new ConcurrentHashMap<>();
    
    /**
     * Constructs a new FileOutputStrategy with the specified base directory.
     * 
     * @param baseDirectory where files will be stored.
     */

    public FileOutputStrategy(String baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    @Override
     /**
     * Outputs the patient data to a file.
     * 
     * @param patientId, Integer representing the ID of the patient. 
     * @param timestamp, Long representing the timestamp of the data.
     * @param label, String of the label associated with the data.
     * @param data, String representing the data to be written to the file.
     */
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            // Create the directory
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
        	// Error handling
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }
        // Set the file path variable, based on the label and ensure it's unique
        // Changed variable name "FilePath" to camelCase
        String filePath =  file_map.computeIfAbsent(label, k -> Paths.get(baseDirectory, label + ".txt").toString());


        // Write the data to the file
        try (PrintWriter out = new PrintWriter(
                Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
                	// Write formatted data to the file
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);
        } catch (IOException e) { // Handle specific exception type
         // Error handling: Print error message if writing to file fails
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
        }
    }
}