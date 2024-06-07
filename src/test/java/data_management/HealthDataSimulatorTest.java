package data_management;

import com.cardio_generator.HealthDataSimulator;
import com.cardio_generator.outputs.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
    void testParseArgumentsWithConsoleOutput() throws IOException, NoSuchFieldException, IllegalAccessException {
        String[] args = {"--output", "console"};
        simulator.parseArguments(args);

        Field outputStrategyField = HealthDataSimulator.class.getDeclaredField("outputStrategy");
        outputStrategyField.setAccessible(true);
        OutputStrategy outputStrategy = (OutputStrategy) outputStrategyField.get(simulator);
        assertTrue(outputStrategy instanceof ConsoleOutputStrategy);
    }

    @Test
    void testParseArgumentsWithFileOutput() throws IOException, NoSuchFieldException, IllegalAccessException {
        String[] args = {"--output", "file:output"};
        simulator.parseArguments(args);

        Field outputStrategyField = HealthDataSimulator.class.getDeclaredField("outputStrategy");
        outputStrategyField.setAccessible(true);
        OutputStrategy outputStrategy = (OutputStrategy) outputStrategyField.get(simulator);
        assertTrue(outputStrategy instanceof FileOutputStrategy);
    }

    @Test
    void testParseArgumentsWithTcpOutput() throws IOException, NoSuchFieldException, IllegalAccessException {
        String[] args = {"--output", "tcp:8080"};
        simulator.parseArguments(args);

        Field outputStrategyField = HealthDataSimulator.class.getDeclaredField("outputStrategy");
        outputStrategyField.setAccessible(true);
        OutputStrategy outputStrategy = (OutputStrategy) outputStrategyField.get(simulator);
        assertTrue(outputStrategy instanceof TcpOutputStrategy);
    }

    @Test
    void testParseArgumentsWithWebSocketOutput() throws IOException, NoSuchFieldException, IllegalAccessException {
        String[] args = {"--output", "websocket:8080"};
        simulator.parseArguments(args);

        Field outputStrategyField = HealthDataSimulator.class.getDeclaredField("outputStrategy");
        outputStrategyField.setAccessible(true);
        OutputStrategy outputStrategy = (OutputStrategy) outputStrategyField.get(simulator);
        assertTrue(outputStrategy instanceof WebSocketOutputStrategy);
    }

    @Test
    void testSchedulerInvocation() throws Exception {
        List<Integer> patientIds = List.of(1, 2, 3, 4, 5);

        Method scheduleTasksForPatients = HealthDataSimulator.class.getDeclaredMethod("scheduleTasksForPatients", List.class);
        scheduleTasksForPatients.setAccessible(true);
        scheduleTasksForPatients.invoke(simulator, patientIds);

        verify(mockScheduler, times(patientIds.size() * 5)).scheduleAtFixedRate(any(Runnable.class), anyLong(), anyLong(), any(TimeUnit.class));
    }
}
