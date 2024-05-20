package data_management;

import com.cardio_generator.HealthDataSimulator;
import com.cardio_generator.outputs.ConsoleOutputStrategy;
import com.cardio_generator.outputs.OutputStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HealthDataSimulatorTest {
    private ScheduledExecutorService mockScheduler;
    private OutputStrategy mockOutputStrategy;
    private HealthDataSimulator simulator;

    @BeforeEach
    void setUp() {
        mockScheduler = mock(ScheduledExecutorService.class);
        mockOutputStrategy = mock(OutputStrategy.class);
        simulator = new HealthDataSimulator(mockScheduler, mockOutputStrategy);
    }

    @Test
    void testParseArgumentsWithPatientCount() throws IOException, NoSuchFieldException, IllegalAccessException {
        String[] args = {"--patient-count", "100"};
        simulator.parseArguments(args);

        Field patientCountField = HealthDataSimulator.class.getDeclaredField("patientCount");
        patientCountField.setAccessible(true);
        int patientCount = (int) patientCountField.get(simulator);
        assertEquals(100, patientCount);
    }

    @Test
    void testParseArgumentsWithInvalidPatientCount() throws IOException, NoSuchFieldException, IllegalAccessException {
        String[] args = {"--patient-count", "invalid"};
        simulator.parseArguments(args);

        Field patientCountField = HealthDataSimulator.class.getDeclaredField("patientCount");
        patientCountField.setAccessible(true);
        int patientCount = (int) patientCountField.get(simulator);
        assertEquals(50, patientCount);
    }

    @Test
    void testParseArgumentsWithOutputStrategyConsole() throws IOException, NoSuchFieldException, IllegalAccessException {
        String[] args = {"--output", "console"};
        simulator.parseArguments(args);

        Field outputStrategyField = HealthDataSimulator.class.getDeclaredField("outputStrategy");
        outputStrategyField.setAccessible(true);
        OutputStrategy outputStrategy = (OutputStrategy) outputStrategyField.get(simulator);
        assertTrue(outputStrategy instanceof ConsoleOutputStrategy);
    }

    @Test
    void testParseArgumentsWithOutputStrategyFile() throws IOException, NoSuchFieldException, IllegalAccessException {
        String[] args = {"--output", "file:output"};
        simulator.parseArguments(args);

        Field outputStrategyField = HealthDataSimulator.class.getDeclaredField("outputStrategy");
        outputStrategyField.setAccessible(true);
        OutputStrategy outputStrategy = (OutputStrategy) outputStrategyField.get(simulator);
        assertTrue(outputStrategy instanceof com.cardio_generator.outputs.FileOutputStrategy);
        Path outputPath = Paths.get("output");
        assertTrue(Files.exists(outputPath));
    }

    @Test
    void testInitializePatientIds() throws Exception {
        Method initializePatientIds = HealthDataSimulator.class.getDeclaredMethod("initializePatientIds", int.class);
        initializePatientIds.setAccessible(true);

        List<Integer> patientIds = (List<Integer>) initializePatientIds.invoke(simulator, 5);
        assertEquals(5, patientIds.size());
        assertTrue(patientIds.contains(1));
        assertTrue(patientIds.contains(5));
    }

    @Test
    void testScheduleTasksForPatients() throws Exception {
        List<Integer> patientIds = List.of(1, 2, 3, 4, 5);

        Method scheduleTasksForPatients = HealthDataSimulator.class.getDeclaredMethod("scheduleTasksForPatients", List.class);
        scheduleTasksForPatients.setAccessible(true);

        scheduleTasksForPatients.invoke(simulator, patientIds);
        verify(mockScheduler, times(patientIds.size() * 5)).scheduleAtFixedRate(any(Runnable.class), anyLong(), anyLong(), any(TimeUnit.class));
    }

    @Test
    void testPrintHelp() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Call the printHelp method using reflection
        try {
            Method printHelpMethod = HealthDataSimulator.class.getDeclaredMethod("printHelp");
            printHelpMethod.setAccessible(true);
            printHelpMethod.invoke(simulator);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred while invoking printHelp method: " + e.getMessage());
        }

        String expectedOutput = "Usage: java HealthDataSimulator [options]\n" +
                "Options:\n" +
                "  -h                       Show help and exit.\n" +
                "  --patient-count <count>  Specify the number of patients to simulate data for (default: 50).\n" +
                "  --output <type>          Define the output method. Options are:\n" +
                "                             'console' for console output,\n" +
                "                             'file:<directory>' for file output,\n" +
                "                             'websocket:<port>' for WebSocket output,\n" +
                "                             'tcp:<port>' for TCP socket output.\n" +
                "Example:\n" +
                "  java HealthDataSimulator --patient-count 100 --output websocket:8080\n" +
                "  This command simulates data for 100 patients and sends the output to WebSocket clients connected to port 8080.\n";

        assertEquals(expectedOutput, outContent.toString());
        System.setOut(originalOut);
    }
}