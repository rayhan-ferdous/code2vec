    public void test36() throws Exception {

        String data = "\"some \\stuff\"";

        CsvReader reader = CsvReader.parse(data);

        reader.setEscapeMode(CsvReader.ESCAPE_MODE_BACKSLASH);

        Assert.assertTrue(reader.readRecord());

        Assert.assertEquals("some stuff", reader.get(0));

        Assert.assertEquals(0L, reader.getCurrentRecord());

        Assert.assertEquals(1, reader.getColumnCount());

        Assert.assertEquals("\"some \\stuff\"", reader.getRawRecord());

        Assert.assertFalse(reader.readRecord());

        reader.close();

    }
