    public void test_18_27_writeObject() {

        Object objToSave = null;

        Object objLoaded;

        try {

            ClassBasedReplacementWhenDumping classBasedReplacementWhenDumping = new ClassBasedReplacementWhenDumping();

            objToSave = classBasedReplacementWhenDumping;

            if (DEBUG) System.out.println("Obj = " + objToSave);

            objLoaded = dumpAndReload(objToSave);

            assertTrue("Did not run writeReplace", classBasedReplacementWhenDumping.calledReplacement);

            assertTrue("Did not replace properly", FOO.equals(objLoaded));

        } catch (IOException e) {

            fail("IOException serializing " + objToSave + " : " + e.getMessage());

        } catch (ClassNotFoundException e) {

            fail("ClassNotFoundException reading Object type : " + e.getMessage());

        } catch (Error err) {

            System.out.println("Error when obj = " + objToSave);

            throw err;

        }

    }
