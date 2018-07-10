    public void testEndTrim() {

        assertEquals("", endTrim(""));

        assertEquals("  x", endTrim("  x  "));

        assertEquals("y", endTrim("y"));

        assertEquals("xaxa", endTrim("xaxa"));

        assertEquals("", endTrim(" "));

        assertEquals("xxx", endTrim("xxx      "));

    }
