package data_management;

import com.cardio_generator.outputs.WebSocketOutputStrategy;
import org.java_websocket.WebSocket;
import org.java_websocket.server.WebSocketServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.net.InetSocketAddress;
import java.util.List;

import static org.mockito.Mockito.*;

public class WebSocketOutputStrategyTest {
    private WebSocketServer mockServer;
    private WebSocket mockWebSocket;
    private WebSocketOutputStrategy outputStrategy;

    @BeforeEach
    public void setUp() {
        mockServer = mock(WebSocketServer.class);
        mockWebSocket = mock(WebSocket.class);
        outputStrategy = new WebSocketOutputStrategy(8080);
        outputStrategy.setServer(mockServer);

        when(mockServer.getConnections()).thenReturn(List.of(mockWebSocket));
    }

    @Test
    public void testOutputValidData() {
        outputStrategy.output(1, 1627842123000L, "HeartRate", "78.0");

        verify(mockWebSocket, times(1)).send("1,1627842123000,HeartRate,78.0");
    }

    @Test
    public void testOutputInvalidData() {
        outputStrategy.output(1, 1627842123000L, null, null);

        verify(mockWebSocket, never()).send(anyString());
    }
}
