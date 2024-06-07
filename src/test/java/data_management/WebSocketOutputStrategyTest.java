package data_management;

import com.cardio_generator.outputs.WebSocketOutputStrategy;
import org.java_websocket.WebSocket;
import org.java_websocket.server.WebSocketServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.net.InetSocketAddress;
import java.util.Set;

import static org.mockito.Mockito.*;

class WebSocketOutputStrategyTest {
    private WebSocketOutputStrategy outputStrategy;
    private WebSocketServer server;
    private WebSocket conn;

    @BeforeEach
    void setUp() throws Exception {
        server = mock(WebSocketServer.class);
        conn = mock(WebSocket.class);
        outputStrategy = new WebSocketOutputStrategy(8080);

        // Using reflection to set the server field
        java.lang.reflect.Field serverField = WebSocketOutputStrategy.class.getDeclaredField("server");
        serverField.setAccessible(true);
        serverField.set(outputStrategy, server);

        // Mocking server connections
        when(server.getConnections()).thenReturn(Set.of(conn));
    }

    @Test
    void testOutputValidData() {
        outputStrategy.output(1, 1627842123000L, "HeartRate", "78.0");
        verify(conn, times(1)).send("1,1627842123000,HeartRate,78.0");
    }
}
