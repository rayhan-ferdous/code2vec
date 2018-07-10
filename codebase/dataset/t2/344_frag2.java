    public void testRunBigDTM_Q16() throws FileNotFoundException, XPathException, SAXException, IOException, XQueryException, URISyntaxException {

        System.gc();

        String o1 = executeQueryWithXbird("q16.xq", "fn:collection('/test/xmark/big_dtm/auction.xml')");

        String o2 = executeQueryWithXbird("q16.xq", "fn:collection('/test/xmark/big_dtms/auction.xml')");

        assertNotNull(o1);

        assertEquals(o1, o2);

        o2 = null;

        String s1 = executeQueryWithSaxon("q16.xq");

        assertEqual(s1, o1);

    }
