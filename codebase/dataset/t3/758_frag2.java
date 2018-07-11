    public void test1() throws Exception {

        CsvReader reader = CsvReader.parse("1,2");

        Assert.assertEquals("", reader.getRawRecord());

        Assert.assertEquals("", reader.get(0));

        Assert.assertTrue(reader.readRecord());

        Assert.assertEquals("1", reader.get(0));

        Assert.assertEquals("2", reader.get(1));

        Assert.assertEquals(',', reader.getDelimiter());

        Assert.assertEquals(0L, reader.getCurrentRecord());

        Assert.assertEquals(2, reader.getColumnCount());

        Assert.assertEquals("1,2", reader.getRawRecord());

        Assert.assertFalse(reader.readRecord());

        Assert.assertEquals("", reader.getRawRecord());

        reader.close();

    }
