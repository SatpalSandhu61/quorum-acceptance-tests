# Gas limit for blocks

 Tags: basic, block, gas-limit

## When block is validated by a node that is party to the transactions, we should not see "BAD BLOCK" caused by "out of gas" error,even though the private transactions in block go over gas limit.
Conditions:
- Block gas limit is set to G1
- Private txn 1 published with gas = G1
- Private txn 2 published with gas = G1
- Minter is not a participant to the transactions
- Block is minted containing txn 1 and 2
- Block is validated by a node that is party to the transactions

 Tags: private,onetestonly

//TODO:
//* Need to set up a network with gas limit = 0x5F5E100 and other genesis changes from issue 576

* Send "20" private transactions where minter is not a participant and gas is equal to gas limit

//TODO:
//* Check all nodes are still running
