        getLog().debug("Check updated balances");

        userTx.begin();

        balances = session.getBalances();

        userTx.commit();

        assertTrue("first balance == 201", balances[0] == 201);

        assertTrue("second balance == 202", balances[1] == 202);

        getLog().debug("Remove PassThrough bean");
