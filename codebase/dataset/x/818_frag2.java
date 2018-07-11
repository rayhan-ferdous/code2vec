    public void test_18_8_writeObject() {

        Object objToSave = null;

        Object objLoaded;

        try {

            double[] doubles = { 0.0, 1.1, 2.2, 3.3 };

            objToSave = doubles;

            if (DEBUG) System.out.println("Obj = " + objToSave);

            objLoaded = dumpAndReload(objToSave);

            assertTrue(MSG_TEST_FAILED + objToSave, Arrays.equals((double[]) objLoaded, (double[]) objToSave));

        } catch (IOException e) {

            fail("IOException serializing data : " + e.getMessage());

        } catch (ClassNotFoundException e) {

            fail("ClassNotFoundException reading Object type : " + e.getMessage());

        } catch (Error err) {

            System.out.println("Error when obj = " + objToSave);

            throw err;

        }

    }
