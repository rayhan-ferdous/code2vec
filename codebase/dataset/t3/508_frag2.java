    public void test_18_22_writeObject() {

        Object objToSave = null;

        Object objLoaded;

        try {

            java.net.URL url = new java.net.URL("http://localhost/a.txt");

            objToSave = url;

            if (DEBUG) System.out.println("Obj = " + objToSave);

            objLoaded = dumpAndReload(objToSave);

            assertTrue("URLs are not the same: " + url + "\t,\t" + objLoaded, url.equals(objLoaded));

        } catch (IOException e) {

            fail("IOException serializing " + objToSave + " : " + e.getMessage());

        } catch (ClassNotFoundException e) {

            fail("ClassNotFoundException reading Object type : " + e.getMessage());

        } catch (Error err) {

            System.out.println("Error when obj = " + objToSave);

            throw err;

        }

    }
