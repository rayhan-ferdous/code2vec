    public void fromDB_BinaryDoc() throws Exception {

        System.out.println("fromDB_BinaryDoc");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        String uri = "xmldb:exist://guest:guest@localhost:8080/exist/xmlrpc/db/" + TESTCASENAME + "/manifest.mf";

        try {

            XmldbURL xmldbUri = new XmldbURL(uri);

            getDocument(xmldbUri, baos);

            assertTrue("Filesize must be greater than 0", baos.size() > 0);

            assertEquals(85, baos.size());

        } catch (Exception ex) {

            ex.printStackTrace();

            LOG.error(ex);

            fail(ex.getCause().getMessage());

        }

    }
