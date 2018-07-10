        balances = frontEnd.getBalances();

        userTx.commit();

        assertTrue("first balance == 101", balances[0] == 101);

        assertTrue("second balance == 102", balances[1] == 102);

        assertTrue("third balance == 103", balances[2] == 103);

        frontEnd.remove();
