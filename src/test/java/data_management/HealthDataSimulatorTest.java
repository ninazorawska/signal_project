package data_management;

import com.cardio_generator.HealthDataSimulator;
import com.cardio_generator.outputs.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class HealthDataSimulatorTest {
    private ScheduledExecutorService mockScheduler;
    private OutputStrategy mockOutputStrategy;
    private HealthDataSimulator simulator;

    @BeforeEach
    void setUp() {
        mockScheduler = mock(ScheduledExecutorService.class);
        mockOutputStrategy = mock(OutputStrategy.class);
        simulator = HealthDataSimulator.getInstance();
    }

    @Test
    void testParseArgumentsWithDefaultValues() throws Exception {
        String[] args = {};
        Method parseArguments = HealthDataSimulator.class.getDeclaredMethod("parseArguments", String[].class);
        parseArguments.setAccessible(true);
        parseArguments.invoke(simulator, (Object) args);

        Field patientCountField = HealthDataSimulator.class.getDeclaredField("patientCount");
        Field outputStrategyField = HealthDataSimulator.class.getDeclaredField("outputStrategy");

        patientCountField.setAccessible(true);
        outputStrategyField.setAccessible(true);

        int patientCount = (int) patientCountField.get(simulator);
        OutputStrategy outputStrategy = (OutputStrategy) outputStrategyField.get(simulator);

        assertEquals(50, patientCount, "Default patient count should be 50");
        assertTrue(outputStrategy instanceof ConsoleOutputStrategy, "Default output strategy should be console");
    }

    @Test
    void testParseArgumentsWithConsoleOutput() throws Exception {
        String[] args = {"--output", "console"};
        Method parseArguments = HealthDataSimulator.class.getDeclaredMethod("parseArguments", String[].class);
        parseArguments.setAccessible(true);
        parseArguments.invoke(simulator, (Object) args);

        Field outputStrategyField = HealthDataSimulator.class.getDeclaredField("outputStrategy");
        outputStrategyField.setAccessible(true);
        OutputStrategy outputStrategy = (OutputStrategy) outputStrategyField.get(simulator);
        assertTrue(outputStrategy instanceof ConsoleOutputStrategy);
    }

    @Test
    void testParseArgumentsWithFileOutput() throws Exception {
        String[] args = {"--output", "file:output"};
        Method parseArguments = HealthDataSimulator.class.getDeclaredMethod("parseArguments", String[].class);
        parseArguments.setAccessible(true);
        parseArguments.invoke(simulator, (Object) args);

        Field outputStrategyField = HealthDataSimulator.class.getDeclaredField("outputStrategy");
        outputStrategyField.setAccessible(true);
        OutputStrategy outputStrategy = (OutputStrategy) outputStrategyField.get(simulator);
        assertTrue(outputStrategy instanceof FileOutputStrategy);
    }

    @Test
    void testParseArgumentsWithTcpOutput() throws Exception {
        String[] args = {"--output", "tcp:8080"};
        Method parseArguments = HealthDataSimulator.class.getDeclaredMethod("parseArguments", String[].class);
        parseArguments.setAccessible(true);
        parseArguments.invoke(simulator, (Object) args);

        Field outputStrategyField = HealthDataSimulator.class.getDeclaredField("outputStrategy");
        outputStrategyField.setAccessible(true);
        OutputStrategy outputStrategy = (OutputStrategy) outputStrategyField.get(simulator);
        assertTrue(outputStrategy instanceof TcpOutputStrategy);
    }

    @Test
    void testParseArgumentsWithWebSocketOutput() throws Exception {
        String[] args = {"--output", "websocket:8080"};
        Method parseArguments = HealthDataSimulator.class.getDeclaredMethod("parseArguments", String[].class);
        parseArguments.setAccessible(true);
        parseArguments.invoke(simulator, (Object) args);

        Field outputStrategyField = HealthDataSimulator.class.getDeclaredField("outputStrategy");
        outputStrategyField.setAccessible(true);
        OutputStrategy outputStrategy = (OutputStrategy) outputStrategyField.get(simulator);
        assertTrue(outputStrategy instanceof WebSocketOutputStrategy);
    }

    @Test
    void testSchedulerInvocation() throws Exception {
        // Inject the mock scheduler into the singleton instance using reflection
        Field schedulerField = HealthDataSimulator.class.getDeclaredField("scheduler");
        schedulerField.setAccessible(true);
        schedulerField.set(simulator, mockScheduler);

        List<Integer> patientIds = List.of(1, 2, 3, 4, 5);

        Method scheduleTasksForPatients = HealthDataSimulator.class.getDeclaredMethod("scheduleTasksForPatients", List.class);
        scheduleTasksForPatients.setAccessible(true);
        scheduleTasksForPatients.invoke(simulator, patientIds);

        verify(mockScheduler, times(patientIds.size() * 5)).scheduleAtFixedRate(any(Runnable.class), anyLong(), anyLong(), any(TimeUnit.class));
    }


    @Test
    void testPrintHelp() throws Exception {
        Method printHelp = HealthDataSimulator.class.getDeclaredMethod("printHelp");
        printHelp.setAccessible(true);

        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        printHelp.invoke(simulator);

        String expectedHelpMessage = "Usage: java HealthDataSimulator [options]\n" +
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
        assertEquals(expectedHelpMessage.trim(), outContent.toString().trim());

        System.setOut(originalOut);
    }
}
