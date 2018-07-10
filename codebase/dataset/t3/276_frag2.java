    public void test_18_125_writeObject() {

        Object objToSave = null;

        Object objLoaded;

        try {

            objToSave = new BigInteger[] { BigInteger.ZERO, BigInteger.ONE, BigInteger.valueOf(-1), BigInteger.valueOf(255), BigInteger.valueOf(-255), new BigInteger("75881644843307850793466070"), new BigInteger("-636104487142732527326202462") };

            if (DEBUG) System.out.println("Obj = " + objToSave);

            objLoaded = dumpAndReload(objToSave);

            assertTrue(MSG_TEST_FAILED + objToSave, Arrays.equals((BigInteger[]) objLoaded, (BigInteger[]) objToSave));

        } catch (IOException e) {

            fail("IOException serializing " + objToSave + " : " + e.getMessage());

        } catch (ClassNotFoundException e) {

            fail("ClassNotFoundException reading Object type : " + e.getMessage());

        } catch (Error err) {

            System.out.println("Error when obj = " + objToSave);

            throw err;

        }

    }
