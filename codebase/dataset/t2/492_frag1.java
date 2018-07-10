    public void test16() throws Exception {

        String data = "\r\r\n1\r";

        CsvReader reader = CsvReader.parse(data);

        reader.setDelimiter('\r');

        Assert.assertTrue(reader.readRecord());

        Assert.assertEquals("", reader.get(0));

        Assert.assertEquals("", reader.get(1));

        Assert.assertEquals("", reader.get(2));

        Assert.assertEquals(0L, reader.getCurrentRecord());

        Assert.assertEquals(3, reader.getColumnCount());

        Assert.assertEquals("\r\r", reader.getRawRecord());

        Assert.assertTrue(reader.readRecord());

        Assert.assertEquals("1", reader.get(0));

        Assert.assertEquals("", reader.get(1));

        Assert.assertEquals(1L, reader.getCurrentRecord());

        Assert.assertEquals(2, reader.getColumnCount());

        Assert.assertEquals("1\r", reader.getRawRecord());

        Assert.assertFalse(reader.readRecord());

        reader.close();

    }
