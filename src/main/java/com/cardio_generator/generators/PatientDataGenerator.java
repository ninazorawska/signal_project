package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Represents a generator interface for generating patient data in health monitoring simulations.
 * Implementing classes are responsible for generating various types of patient data, such as vital signs and alerts.
 * 
 * <p>
 * Implementing classes must provide an implementation for the {@code generate} method,
 * which generates patient data for a specified patient ID and outputs it using the provided {@link OutputStrategy}.
 * </p>
 * 
 */
public interface PatientDataGenerator {

    /**
     * Generates patient data for the specific patient and outputs it using the provided OutputStrategy.
     * 
     * @param patientId      Integer representing the ID of the patient for whom data is generated.
     * @param outputStrategy The output strategy to use for data output (OutputStrategy).
     */
    void generate(int patientId, OutputStrategy outputStrategy);
}
