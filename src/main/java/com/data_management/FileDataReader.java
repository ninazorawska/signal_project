package com.data_management;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileDataReader implements DataReader {

    @Override
    public void readData(Path directoryPath, DataStorage dataStorage) throws IOException {
        // Ensure the directory exists
        if (!Files.isDirectory(directoryPath)) {
            throw new IllegalArgumentException("Specified path is not a directory: " + directoryPath);
        }

        // Get a list of files in the directory
        List<Path> files = Files.list(directoryPath).toList();

        // Iterate through each file
        for (Path file : files) {
            if (Files.isRegularFile(file)) {
                // Process each regular file
                readDataFromFile(file.toFile(), dataStorage);
            }
        }
    }

    private void readDataFromFile(File file, DataStorage dataStorage) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Assuming each line represents a data entry
                // Split the line and extract relevant data
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    int patientId = Integer.parseInt(parts[0]);
                    double measurementValue = Double.parseDouble(parts[1]);
                    String measurementType = parts[2];
                    long timestamp = Long.parseLong(parts[3]);
                    
                    // Create a PatientRecord and add it to the DataStorage
                    dataStorage.addPatientData(patientId, measurementValue, measurementType, timestamp);
                }
            }
        }
    }
}
