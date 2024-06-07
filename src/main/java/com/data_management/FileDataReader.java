package com.data_management;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class FileDataReader implements DataReader {
    private final Path directoryPath;

    public FileDataReader(Path directoryPath) {
        if (!Files.isDirectory(directoryPath)) {
            throw new IllegalArgumentException("Specified path is not a directory: " + directoryPath);
        }
        this.directoryPath = directoryPath;
    }

    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        // Iterate through files in the directory
        try (Stream<Path> stream = Files.list(directoryPath)) {
            stream.filter(Files::isRegularFile)
                  .forEach(file -> parseAndStoreData(file, dataStorage));
        }
    }

    public void parseAndStoreData(Path filePath, DataStorage dataStorage) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Parse each line to extract data
                // Assuming data format: <patientId>,<measurementValue>,<recordType>,<timestamp>
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    int patientId = Integer.parseInt(parts[0].trim());
                    double measurementValue = Double.parseDouble(parts[1].trim());
                    String recordType = parts[2].trim();
                    long timestamp = Long.parseLong(parts[3].trim());

                    // Add parsed data to DataStorage
                    dataStorage.addPatientData(patientId, measurementValue, recordType, timestamp);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath);
            e.printStackTrace();
        }
    }
}
