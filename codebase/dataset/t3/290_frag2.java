    public void test10() throws Exception {

        String data = "1\r2\n";

        CsvReader reader = CsvReader.parse(data);

        reader.setDelimiter('\r');

        Assert.assertTrue(reader.readRecord());

        Assert.assertEquals("1", reader.get(0));

        Assert.assertEquals("2", reader.get(1));

        Assert.assertEquals(0L, reader.getCurrentRecord());

        Assert.assertEquals(2, reader.getColumnCount());

        Assert.assertEquals("1\r2", reader.getRawRecord());

        Assert.assertFalse(reader.readRecord());

        Assert.assertEquals("", reader.getRawRecord());

        reader.close();

    }
