    public void testRunBigDTM_Q10() throws FileNotFoundException, XPathException, SAXException, IOException, XQueryException, URISyntaxException {

        System.gc();

        String o1 = executeQueryWithXbird("q10.xq", "fn:collection('/test/xmark/big_dtm/auction.xml')");

        String o2 = executeQueryWithXbird("q10.xq", "fn:collection('/test/xmark/big_dtms/auction.xml')");

        assertNotNull(o1);

        assertEquals(o1, o2);

        o2 = null;

        String s1 = executeQueryWithSaxon("q10.xq");

        assertEqual(s1, o1);

    }
