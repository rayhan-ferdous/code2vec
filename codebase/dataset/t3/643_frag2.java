        userTx.commit();

        assertTrue("first balance == 201", balances[0] == 201);

        assertTrue("second balance == 202", balances[1] == 202);

        assertTrue("third balance == 203", balances[2] == 203);

        getLog().debug("Remove FrontEnd bean");

        frontEnd.remove();

    }
