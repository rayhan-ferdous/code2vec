    public void test_18_127_writeObject() {

        Object objToSave = null;

        Object objLoaded;

        try {

            BitSet bs = new BitSet(64);

            bs.set(1);

            bs.set(10);

            bs.set(100);

            bs.set(1000);

            objToSave = bs;

            if (DEBUG) System.out.println("Obj = " + objToSave);

            objLoaded = dumpAndReload(objToSave);

            assertTrue(MSG_TEST_FAILED + objToSave, bs.equals(objLoaded));

        } catch (IOException e) {

            fail("IOException serializing " + objToSave + " : " + e.getMessage());

        } catch (ClassNotFoundException e) {

            fail("ClassNotFoundException reading Object type : " + e.getMessage());

        } catch (Error err) {

            System.out.println("Error when obj = " + objToSave);

            throw err;

        }

    }
