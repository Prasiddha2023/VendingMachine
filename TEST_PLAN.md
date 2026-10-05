# Test Plan

# do ctrl shift v to view


| Method / Behavior | Valid Case(s) | Exception / Invalid Case(s) | Boundary Case(s) | Oracle / Expected Result | Related JUnit Test(s) |
|---|---|---|---|---|---|
| VendingMachine() | new machine | N/A | N/A | Javadoc: all slots null, balance 0 | testConstructor_newMachine_balanceIsZero, testConstructor_newMachine_allSlotsEmpty |
| addItem | add to empty slot A | occupied slot; bad codes E, a, empty, AB | A, D valid; E invalid | Javadoc: item stored; throws if occupied or invalid code | testAddItem_emptySlot_itemStored, testAddItem_occupiedSlot_throwsException, testAddItem_invalidCode_throwsException, testSlotCode_validAndInvalid |
| removeItem | remove from occupied slot | empty slot; invalid code | occupied to empty | Javadoc: returns item, slot empty; throws if empty or invalid | testRemoveItem_occupiedSlot_returnsItem, testRemoveItem_occupiedSlot_slotEmptied, testRemoveItem_emptySlot_throwsException, testRemoveItem_invalidCode_throwsException |
| insertMoney | 5.00 then 2.50 | negative amounts | -0.01, 0, 0.01, 0.99, 1.0, 1.01 | Javadoc: amount >= 0, balance = old + amount, throws if < 0 | testInsertMoney_validAmount_increasesBalance, testInsertMoney_zero_allowedAndBalanceUnchanged, testInsertMoney_negative_throwsException, testInsertMoney_amounts_addedToBalance, testInsertMoney_negativeAmounts_throw |
| getBalance | after inserting 3.00 | N/A | new machine = 0 | Javadoc: balance unchanged by call | testGetBalance_calledTwice_unchanged |
| makePurchase | balance 2.00, price 1.50: true, balance 0.50 | empty slot: false | balance 1.49, 1.50, 1.51 | Javadoc: true if enough money, false if not or empty; hand calculation | testMakePurchase_moreThanPrice_succeedsAndSubtractsPrice, testMakePurchase_exactBalance_succeeds, testMakePurchase_insufficientBalance_failsAndKeepsItem, testMakePurchase_emptySlot_returnsFalse, testMakePurchase_balanceBoundary |
| returnChange | balance 4.25 returns 4.25 | N/A | balance 0 | Javadoc: returns balance, then balance = 0 | testReturnChange_withBalance_returnsAndZeroesBalance, testReturnChange_zeroBalance_returnsZero, testReturnChange_afterPurchase_returnsRemainder |
| VendingMachineItem | "Chips", 1.25 | price -0.01 | -0.01 invalid, 0 valid | Javadoc: price >= 0, throws if < 0 | testConstructor_validValues_storedCorrectly, testConstructor_zeroPrice_allowed, testConstructor_negativePrice_throwsException |
| VendingMachineException | message "reason" | N/A | no message | Javadoc: carries reason; extends RuntimeException | testMessageConstructor_returnsMessage, testDefaultConstructor_messageIsNull, testException_isRuntimeException |

## Parameterized tests (in VendingMachineTest)

- testInsertMoney_amounts_addedToBalance: values 0, 0.01, 0.99, 1.0, 1.01, 100. Tests the lower boundary and values around 1.
- testMakePurchase_balanceBoundary: balances 0, 1.0, 1.49, 1.5, 1.51, 3.0 with price 1.50. Tests below, at, and above the price.
- testSlotCode_validAndInvalid: A, B, C, D, E, a, empty, AB. Tests every valid slot and several invalid codes.
- testInsertMoney_negativeAmounts_throw: -0.01, -0.5, -1, -100. All negative, so all must throw.