    public void test43() throws Exception {

        String data = "\"line 1\\nline 2\",\"line 1\\\nline 2\"";

        CsvReader reader = CsvReader.parse(data);

        reader.setEscapeMode(CsvReader.ESCAPE_MODE_BACKSLASH);

        Assert.assertTrue(reader.readRecord());

        Assert.assertEquals("line 1\nline 2", reader.get(0));

        Assert.assertEquals("line 1\nline 2", reader.get(1));

        Assert.assertEquals(0L, reader.getCurrentRecord());

        Assert.assertEquals(2, reader.getColumnCount());

        Assert.assertFalse(reader.readRecord());

        reader.close();

    }
