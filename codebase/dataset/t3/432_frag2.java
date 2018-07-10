    public void test29() throws Exception {

        String data = "\"double\\\"\\\"double quotes\"";

        CsvReader reader = CsvReader.parse(data);

        reader.setEscapeMode(CsvReader.ESCAPE_MODE_BACKSLASH);

        Assert.assertTrue(reader.readRecord());

        Assert.assertEquals("double\"\"double quotes", reader.get(0));

        Assert.assertEquals(0L, reader.getCurrentRecord());

        Assert.assertEquals(1, reader.getColumnCount());

        Assert.assertEquals("\"double\\\"\\\"double quotes\"", reader.getRawRecord());

        Assert.assertFalse(reader.readRecord());

        reader.close();

    }
