    public void test41() throws Exception {

        String data = "double\\\\\\\\double backslash";

        CsvReader reader = CsvReader.parse(data);

        reader.setUseTextQualifier(false);

        reader.setEscapeMode(CsvReader.ESCAPE_MODE_BACKSLASH);

        Assert.assertTrue(reader.readRecord());

        Assert.assertEquals("double\\\\double backslash", reader.get(0));

        Assert.assertEquals(0L, reader.getCurrentRecord());

        Assert.assertEquals(1, reader.getColumnCount());

        Assert.assertFalse(reader.readRecord());

        reader.close();

    }
