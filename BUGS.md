## bug number 1

•	The observed failure:

Almost every VendingMachineTest test errored with `ArrayIndexOutOfBoundsException: Index 4 out of bounds for length 4`.

•	The test that exposed it:

`testConstructor_newMachine_balanceIsZero` (and all other VendingMachineTest tests, because `setUp()` creates a new VendingMachine).


•	The source-code fault that caused it:

`VendingMachine.java`, in the `VendingMachine()` constructor, line 63. The loop was `for (int i = 0; i <= NUM_SLOTS; i++)`. The array has 4 slots (indexes 0 to 3), so `<=` makes the loop run with i = 4.


•	How you diagnosed the fault:

I set a breakpoint on line 63 and debugged `testConstructor_newMachine_balanceIsZero`. Stepping through, `i` reached 4 while `itemArray.length` was 4, and `itemArray[4] = null` on line 64 threw the exception.


•	The correction you made:

Changed line 63 from `i <= NUM_SLOTS` to `i < NUM_SLOTS`.


## bug number 2
•	The observed failure:

`VendingMachineException: Invalid amount. Amount must be >= 0` when inserting 0.0, 0.01, and 0.99.

•	The test that exposed it:

`testInsertMoney_zero_allowedAndBalanceUnchanged`, the 0.0, 0.01 and 0.99 rows of `testInsertMoney_amounts_addedToBalance`, and the 0.0 row of `testMakePurchase_balanceBoundary`.

•	The source-code fault that caused it:

`VendingMachine.java`, `insertMoney()`, line 161: `if (amount < 1)`. The Javadoc says amount >= 0 is valid.


•	How you diagnosed the fault:

I set a breakpoint on line 161 and debugged the zero test. `amount` was 0.0 and the condition was true, so the exception was thrown. I compared this to the Javadoc, which allows 0.

•	The correction you made:

Changed `amount < 1` to `amount < 0`.
