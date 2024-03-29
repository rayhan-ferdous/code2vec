    public void test_18_14_writeObject() {

        Object objToSave = null;

        Object objLoaded;

        try {

            SpecTest specTest = new SpecTest();

            specTest.instVar = FOO;

            specTest.instVar1 = specTest.instVar;

            objToSave = specTest;

            if (DEBUG) System.out.println("Obj = " + objToSave);

            objLoaded = dumpAndReload(objToSave);

            assertNull(MSG_TEST_FAILED + objToSave, ((SpecTest) objLoaded).instVar);

            assertTrue(MSG_TEST_FAILED + objToSave, ((SpecTest) objLoaded).instVar1.equals(FOO));

        } catch (IOException e) {

            e.printStackTrace();

            fail("Exception serializing " + objToSave + "\t->" + e.toString());

        } catch (ClassNotFoundException e) {

            fail("ClassNotFoundException reading Object type : " + e.getMessage());

        } catch (Error err) {

            System.out.println("Error when obj = " + objToSave);

            throw err;

        }

    }
