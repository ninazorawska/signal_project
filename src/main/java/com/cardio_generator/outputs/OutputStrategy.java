package com.cardio_generator.outputs;

/**
 * Represents an interface for output strategies in health monitoring simulations.
 * Implementing classes define specific strategies for outputting patient data.
 * 
 * <p>
 * Implementing classes must provide an implementation for the {@code output} method.
 * </p>
 * 
 */
public interface OutputStrategy {

    /**
     * Outputs patient data using the specified parameters.
     * 
     * @param patientId Integer representing the ID of the patient for whom data is being outputted.
     * @param timestamp Long representing timestamp indicating when the data was generated.
     * @param label     String representing the label describing the type of data being outputted.
     * @param data      String representing data being outputted.
     */
    void output(int patientId, long timestamp, String label, String data);
}
