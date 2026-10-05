import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class VendingMachineItemTest {

    private static final double DELTA = 0.0001;

    @Test
    public void testConstructor_validValues_storedCorrectly() {
        // Arrange
        String name = "Chips";
        double price = 1.25;

        // Act
        VendingMachineItem item = new VendingMachineItem(name, price);

        // Assert
        assertEquals("Chips", item.getName());
        assertEquals(1.25, item.getPrice(), DELTA);
    }

    @Test
    public void testConstructor_zeroPrice_allowed() {
        // Arrange
        String name = "Free sample";
        double price = 0.0;

        // Act
        VendingMachineItem item = new VendingMachineItem(name, price);

        // Assert
        assertEquals(0.0, item.getPrice(), DELTA);
    }

    @Test
    public void testConstructor_negativePrice_throwsException() {
        // Arrange
        String name = "Bad";
        double price = -0.01;

        // Act + Assert
        assertThrows(VendingMachineException.class,
                () -> new VendingMachineItem(name, price));
    }
}