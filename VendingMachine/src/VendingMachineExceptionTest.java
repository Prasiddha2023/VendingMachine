import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class VendingMachineExceptionTest {

    @Test
    public void testMessageConstructor_returnsMessage() {
        // Arrange
        String reason = "reason";

        // Act
        VendingMachineException e = new VendingMachineException(reason);

        // Assert
        assertEquals("reason", e.getMessage());
    }

    @Test
    public void testDefaultConstructor_messageIsNull() {
        // Arrange (nothing to set up)

        // Act
        VendingMachineException e = new VendingMachineException();

        // Assert
        assertNull(e.getMessage());
    }

    @Test
    public void testException_isRuntimeException() {
        // Arrange (nothing to set up)

        // Act
        VendingMachineException e = new VendingMachineException();

        // Assert
        assertTrue(e instanceof RuntimeException);
    }
}