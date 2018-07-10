    @Test

    public void test32() throws Exception {

        String data = "\"Mac \"The Knife\" Peter\",\"Boswell, Jr.\"";

        CsvReader reader = CsvReader.parse(data);

        Assert.assertTrue(reader.readRecord());

        Assert.assertEquals("Mac ", reader.get(0));

        Assert.assertEquals("Boswell, Jr.", reader.get(1));

        Assert.assertEquals(0L, reader.getCurrentRecord());

        Assert.assertEquals(2, reader.getColumnCount());

        Assert.assertEquals("\"Mac \"The Knife\" Peter\",\"Boswell, Jr.\"", reader.getRawRecord());

        Assert.assertFalse(reader.readRecord());

        reader.close();

    }
