    public void test25() throws Exception {

        String data = "1\r\n# bunch of crazy stuff here\r\n1";

        CsvReader reader = CsvReader.parse(data);

        reader.setUseTextQualifier(false);

        reader.setUseComments(true);

        Assert.assertTrue(reader.readRecord());

        Assert.assertEquals("1", reader.get(0));

        Assert.assertEquals(0L, reader.getCurrentRecord());

        Assert.assertEquals(1, reader.getColumnCount());

        Assert.assertEquals("1", reader.getRawRecord());

        Assert.assertTrue(reader.readRecord());

        Assert.assertEquals("1", reader.get(0));

        Assert.assertEquals(1L, reader.getCurrentRecord());

        Assert.assertEquals(1, reader.getColumnCount());

        Assert.assertEquals("1", reader.getRawRecord());

        Assert.assertFalse(reader.readRecord());

        reader.close();

    }
