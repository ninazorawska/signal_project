package data_management;

import org.junit.jupiter.api.Test;

import com.alerts.Alert;

import static org.junit.jupiter.api.Assertions.*;

class AlertTest {

    @Test
    void testAlertConstructorAndGetters() {
        Alert alert = new Alert("1", "High Heart Rate", 1714376789050L);

        assertEquals("1", alert.getPatientId());
        assertEquals("High Heart Rate", alert.getCondition());
        assertEquals(1714376789050L, alert.getTimestamp());
    }
}
