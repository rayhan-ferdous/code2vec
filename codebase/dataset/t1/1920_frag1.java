    @Test

    public void test138() throws Exception {

        CsvReader reader = CsvReader.parse("1;; ;1");

        reader.setRecordDelimiter(';');

        Assert.assertTrue(reader.getSkipEmptyRecords());

        Assert.assertTrue(reader.readRecord());

        Assert.assertEquals(1, reader.getColumnCount());

        Assert.assertEquals("1", reader.get(0));

        Assert.assertEquals(0L, reader.getCurrentRecord());

        Assert.assertTrue(reader.readRecord());

        Assert.assertEquals(1, reader.getColumnCount());

        Assert.assertEquals("", reader.get(0));

        Assert.assertEquals(1L, reader.getCurrentRecord());

        Assert.assertTrue(reader.readRecord());

        Assert.assertEquals(1, reader.getColumnCount());

        Assert.assertEquals("1", reader.get(0));

        Assert.assertEquals(2L, reader.getCurrentRecord());

        Assert.assertFalse(reader.readRecord());

        reader.close();

    }
