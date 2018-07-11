    public void test_18_15_writeObject() {

        Object objToSave = null;

        Object objLoaded;

        try {

            SpecTestSubclass specTestSubclass = new SpecTestSubclass();

            specTestSubclass.transientInstVar = FOO;

            objToSave = specTestSubclass;

            if (DEBUG) System.out.println("Obj = " + objToSave);

            objLoaded = dumpAndReload(objToSave);

            assertNull(MSG_TEST_FAILED + objToSave, ((SpecTestSubclass) objLoaded).transientInstVar);

        } catch (IOException e) {

            fail("Exception serializing " + objToSave + "\t->" + e.toString());

        } catch (ClassNotFoundException e) {

            fail("ClassNotFoundException reading Object type : " + e.getMessage());

        } catch (Error err) {

            System.out.println("Error when obj = " + objToSave);

            throw err;

        }

    }
