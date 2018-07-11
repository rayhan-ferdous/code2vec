    public void test14() throws Exception {

        String data = "user_id,name\r\n1,Bruce";

        CsvReader reader = CsvReader.parse(data);

        Assert.assertTrue(reader.readHeaders());

        Assert.assertEquals("user_id,name", reader.getRawRecord());

        Assert.assertTrue(reader.readRecord());

        Assert.assertEquals("1", reader.get(0));

        Assert.assertEquals("Bruce", reader.get(1));

        Assert.assertEquals(0L, reader.getCurrentRecord());

        Assert.assertEquals(2, reader.getColumnCount());

        Assert.assertEquals(0, reader.getIndex("user_id"));

        Assert.assertEquals(1, reader.getIndex("name"));

        Assert.assertEquals("user_id", reader.getHeader(0));

        Assert.assertEquals("name", reader.getHeader(1));

        Assert.assertEquals("1", reader.get("user_id"));

        Assert.assertEquals("Bruce", reader.get("name"));

        Assert.assertEquals("1,Bruce", reader.getRawRecord());

        Assert.assertFalse(reader.readRecord());

        reader.close();

    }
