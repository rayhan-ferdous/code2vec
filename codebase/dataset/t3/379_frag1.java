    public void test15() throws Exception {

        String data = "\"data \r\n here\"";

        CsvReader reader = CsvReader.parse(data);

        Assert.assertTrue(reader.readRecord());

        Assert.assertEquals("data \r\n here", reader.get(0));

        Assert.assertEquals(0L, reader.getCurrentRecord());

        Assert.assertEquals(1, reader.getColumnCount());

        Assert.assertEquals("\"data \r\n here\"", reader.getRawRecord());

        Assert.assertFalse(reader.readRecord());

        reader.close();

    }
