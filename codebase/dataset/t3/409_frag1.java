    public void test_18_109_writeObject() {

        Object objToSave = null;

        Object objLoaded;

        try {

            objToSave = CALENDAR;

            if (DEBUG) System.out.println("Obj = " + objToSave);

            objLoaded = dumpAndReload(objToSave);

            assertTrue(MSG_TEST_FAILED + objToSave, CALENDAR.equals(objLoaded));

        } catch (IOException e) {

            fail("IOException serializing " + objToSave + " : " + e.getMessage());

        } catch (ClassNotFoundException e) {

            fail("ClassNotFoundException reading Object type : " + e.getMessage());

        } catch (Error err) {

            System.out.println("Error when obj = " + objToSave);

            throw err;

        }

    }
