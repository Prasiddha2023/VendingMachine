import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class VendingMachineTest {

    private static final double DELTA = 0.0001;

    VendingMachine machine;
    VendingMachineItem chips; // price 1.50

    @BeforeEach
    public void setUp() {
        machine = new VendingMachine();
        chips = new VendingMachineItem("Chips", 1.50);
    }

    // ---------- constructor ----------

    @Test
    public void testConstructor_newMachine_balanceIsZero() {
        // Arrange (done in setUp)

        // Act
        double balance = machine.getBalance();

        // Assert
        assertEquals(0.0, balance, DELTA);
    }

    @Test
    public void testConstructor_newMachine_allSlotsEmpty() {
        // Arrange (done in setUp)

        // Act
        VendingMachineItem a = machine.getItem("A");
        VendingMachineItem b = machine.getItem("B");
        VendingMachineItem c = machine.getItem("C");
        VendingMachineItem d = machine.getItem("D");

        // Assert
        assertNull(a);
        assertNull(b);
        assertNull(c);
        assertNull(d);
    }

    // ---------- addItem ----------

    @Test
    public void testAddItem_emptySlot_itemStored() {
        // Arrange (machine is empty)

        // Act
        machine.addItem(chips, "A");

        // Assert
        assertSame(chips, machine.getItem("A"));
    }

    @Test
    public void testAddItem_occupiedSlot_throwsException() {
        // Arrange
        machine.addItem(chips, "B");
        VendingMachineItem gum = new VendingMachineItem("Gum", 0.50);

        // Act + Assert
        assertThrows(VendingMachineException.class,
                () -> machine.addItem(gum, "B"));
    }

    @Test
    public void testAddItem_invalidCode_throwsException() {
        // Arrange
        String badCode = "E";

        // Act + Assert
        assertThrows(VendingMachineException.class,
                () -> machine.addItem(chips, badCode));
    }

    @ParameterizedTest
    @CsvSource({
        "A, true", "B, true", "C, true", "D, true",
        "E, false", "a, false", "'', false", "AB, false"
    })
    public void testSlotCode_validAndInvalid(String code, boolean valid) {
        // Arrange (machine and item from setUp)

        // Act + Assert
        if (valid) {
            machine.addItem(chips, code);
            assertSame(chips, machine.getItem(code));
        } else {
            assertThrows(VendingMachineException.class,
                    () -> machine.addItem(chips, code));
        }
    }

    // ---------- removeItem ----------

    @Test
    public void testRemoveItem_occupiedSlot_returnsItem() {
        // Arrange
        machine.addItem(chips, "C");

        // Act
        VendingMachineItem removed = machine.removeItem("C");

        // Assert
        assertSame(chips, removed);
    }

    @Test
    public void testRemoveItem_occupiedSlot_slotEmptied() {
        // Arrange
        machine.addItem(chips, "C");

        // Act
        machine.removeItem("C");

        // Assert
        assertNull(machine.getItem("C"));
    }

    @Test
    public void testRemoveItem_emptySlot_throwsException() {
        // Arrange (slot D is empty)

        // Act + Assert
        assertThrows(VendingMachineException.class,
                () -> machine.removeItem("D"));
    }

    @Test
    public void testRemoveItem_invalidCode_throwsException() {
        // Arrange
        String badCode = "Z";

        // Act + Assert
        assertThrows(VendingMachineException.class,
                () -> machine.removeItem(badCode));
    }

    // ---------- insertMoney / getBalance ----------

    @Test
    public void testInsertMoney_validAmount_increasesBalance() {
        // Arrange
        double first = 5.00;
        double second = 2.50;

        // Act
        machine.insertMoney(first);
        machine.insertMoney(second);

        // Assert
        assertEquals(7.50, machine.getBalance(), DELTA);
    }

    @Test
    public void testInsertMoney_zero_allowedAndBalanceUnchanged() {
        // Arrange
        double amount = 0.0;

        // Act
        machine.insertMoney(amount);

        // Assert
        assertEquals(0.0, machine.getBalance(), DELTA);
    }

    @Test
    public void testInsertMoney_negative_throwsException() {
        // Arrange
        double amount = -0.01;

        // Act + Assert
        assertThrows(VendingMachineException.class,
                () -> machine.insertMoney(amount));
    }

    @ParameterizedTest
    @CsvSource({ "0.0", "0.01", "0.99", "1.0", "1.01", "100.0" })
    public void testInsertMoney_amounts_addedToBalance(double amount) {
        // Arrange (fresh machine, balance 0)

        // Act
        machine.insertMoney(amount);

        // Assert
        assertEquals(amount, machine.getBalance(), DELTA);
    }

    @ParameterizedTest
    @ValueSource(doubles = { -0.01, -0.5, -1.0, -100.0 })
    public void testInsertMoney_negativeAmounts_throw(double amount) {
        // Arrange (fresh machine)

        // Act + Assert
        assertThrows(VendingMachineException.class,
                () -> machine.insertMoney(amount));
    }

    @Test
    public void testGetBalance_calledTwice_unchanged() {
        // Arrange
        machine.insertMoney(3.00);

        // Act
        double first = machine.getBalance();
        double second = machine.getBalance();

        // Assert
        assertEquals(3.00, first, DELTA);
        assertEquals(first, second, DELTA);
    }

    // ---------- makePurchase ----------

    @Test
    public void testMakePurchase_moreThanPrice_succeedsAndSubtractsPrice() {
        // Arrange
        machine.addItem(chips, "A");
        machine.insertMoney(2.00);

        // Act
        boolean result = machine.makePurchase("A");

        // Assert
        assertTrue(result);
        assertEquals(0.50, machine.getBalance(), DELTA);
        assertNull(machine.getItem("A"));
    }

    @Test
    public void testMakePurchaseExactBalanceSucceeds() {
        // Arrange
        machine.addItem(chips, "A");
        machine.insertMoney(1.50);

        // Act
        boolean result = machine.makePurchase("A");

        // Assert
        assertTrue(result);
        assertEquals(0.0, machine.getBalance(), DELTA);
    }

    @Test
    public void testMakePurchase_insufficientBalance_failsAndKeepsItem() {
        // Arrange
        machine.addItem(chips, "A");
        machine.insertMoney(1.49);

        // Act
        boolean result = machine.makePurchase("A");

        // Assert
        assertFalse(result);
        assertEquals(1.49, machine.getBalance(), DELTA);
        assertSame(chips, machine.getItem("A"));
    }

    @Test
    public void testMakePurchase_emptySlot_returnsFalse() {
        // Arrange
        machine.insertMoney(5.00);

        // Act
        boolean result = machine.makePurchase("B");

        // Assert
        assertFalse(result);
        assertEquals(5.00, machine.getBalance(), DELTA);
    }

    @ParameterizedTest
    @CsvSource({
        // balance, expectedResult, expectedRemaining   (price = 1.50)
        "0.0,  false, 0.0",
        "1.0,  false, 1.0",
        "1.49, false, 1.49",
        "1.5,  true,  0.0",
        "1.51, true,  0.01",
        "3.0,  true,  1.5"
    })
    public void testMakePurchase_balanceBoundary(double balance, boolean expected, double remaining) {
        // Arrange
        machine.addItem(chips, "A");
        machine.insertMoney(balance);

        // Act
        boolean result = machine.makePurchase("A");

        // Assert
        assertEquals(expected, result);
        assertEquals(remaining, machine.getBalance(), DELTA);
    }

    // ---------- returnChange ----------

    @Test
    public void testReturnChange_withBalance_returnsAndZeroesBalance() {
        // Arrange
        machine.insertMoney(4.25);

        // Act
        double change = machine.returnChange();

        // Assert
        assertEquals(4.25, change, DELTA);
        assertEquals(0.0, machine.getBalance(), DELTA);
    }

    @Test
    public void testReturnChange_zeroBalance_returnsZero() {
        // Arrange (balance is 0)

        // Act
        double change = machine.returnChange();

        // Assert
        assertEquals(0.0, change, DELTA);
    }

    @Test
    public void testReturnChange_afterPurchase_returnsRemainder() {
        // Arrange
        machine.addItem(chips, "A");
        machine.insertMoney(2.00);
        machine.makePurchase("A");

        // Act
        double change = machine.returnChange();

        // Assert
        assertEquals(0.50, change, DELTA);
    }
}