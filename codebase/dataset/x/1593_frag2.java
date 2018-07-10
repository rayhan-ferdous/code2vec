    @Test

    public void test35() throws Exception {

        String data = "Chicane, Love on the Run, Knight Rider, \"This field contains a comma, but it doesn't matter as the field is quoted\"";

        CsvReader reader = CsvReader.parse(data);

        Assert.assertTrue(reader.readRecord());

        Assert.assertEquals("Chicane", reader.get(0));

        Assert.assertEquals("Love on the Run", reader.get(1));

        Assert.assertEquals("Knight Rider", reader.get(2));

        Assert.assertEquals("This field contains a comma, but it doesn't matter as the field is quoted", reader.get(3));

        Assert.assertEquals(0L, reader.getCurrentRecord());

        Assert.assertEquals(4, reader.getColumnCount());

        Assert.assertEquals("Chicane, Love on the Run, Knight Rider, \"This field contains a comma, but it doesn't matter as the field is quoted\"", reader.getRawRecord());

        Assert.assertFalse(reader.readRecord());

        reader.close();

    }
