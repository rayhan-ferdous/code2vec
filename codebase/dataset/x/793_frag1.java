    @Test

    public void test7() throws Exception {

        String data = "1\r";

        CsvReader reader = CsvReader.parse(data);

        Assert.assertTrue(reader.readRecord());

        Assert.assertEquals("1", reader.get(0));

        Assert.assertEquals(0L, reader.getCurrentRecord());

        Assert.assertEquals(1, reader.getColumnCount());

        Assert.assertEquals("1", reader.getRawRecord());

        Assert.assertFalse(reader.readRecord());

        Assert.assertEquals("", reader.getRawRecord());

        reader.close();

    }
