package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;

    /**
     * Generates alert data for patients.
     */
public class AlertGenerator implements PatientDataGenerator {
    
    // Changed variable name to camelCase 
    public static final Random randomGenerator = new Random();

    // Changed variable name to camelCase
    private boolean[] alertStates; // false = resolved, true = pressed

    /**
     * Constructs an AlertGenerator object with the specified number of patients.
     *
     * @param patientCount Integer representing number of patients.
     */
    public AlertGenerator(int patientCount) {
        alertStates = new boolean[patientCount + 1];
    }

    /**
     * Creates alert data for the specified patient and outputs it using the provided OutputStrategy.
     *
     * @param patientId      Integer representing the ID of the patient.
     * @param outputStrategy The output strategy to use for data output.
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            if (alertStates[patientId]) {
                if (randomGenerator.nextDouble() < 0.9) { // 90% chance to resolve
                    alertStates[patientId] = false;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "resolved");
                }
            } else {
                // Changed variables names to camelCase 
                double lambda = 0.1; // Average rate (alerts per period), adjust based on desired frequency
                double p = -Math.expm1(-lambda); // Probability of at least one alert in the period
                boolean alertTriggered = randomGenerator.nextDouble() < p;

                if (alertTriggered) {
                    alertStates[patientId] = true;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "triggered");
                }
            }
        // Error handling
        } catch (Exception e) {
            System.err.println("An error occurred while generating alert data for patient " + patientId);
            e.printStackTrace();
        }
    }
}
