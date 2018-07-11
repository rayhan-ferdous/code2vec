    public void test146() throws Exception {

        CsvReader reader = CsvReader.parse("\"" + generateString('a', 10000) + "\r\nb");

        Assert.assertEquals("", reader.getRawRecord());

        Assert.assertTrue(reader.skipLine());

        Assert.assertEquals("", reader.getRawRecord());

        Assert.assertTrue(reader.readRecord());

        Assert.assertEquals("b", reader.get(0));

        Assert.assertFalse(reader.readRecord());

        reader.close();

    }
