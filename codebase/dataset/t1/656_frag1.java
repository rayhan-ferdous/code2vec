    public void test45() throws Exception {

        String data = "\"Chicane\", \"Love on the Run\", \"Knight Rider\", \"This field contains a comma, but it doesn't matter as the field is quoted\"i" + "\"Samuel Barber\", \"Adagio for Strings\", \"Classical\", \"This field contains a double quote character, \"\", but it doesn't matter as it is escaped\"";

        CsvReader reader = CsvReader.parse(data);

        Assert.assertTrue(reader.getCaptureRawRecord());

        reader.setCaptureRawRecord(false);

        Assert.assertFalse(reader.getCaptureRawRecord());

        Assert.assertEquals("", reader.getRawRecord());

        reader.setRecordDelimiter('i');

        Assert.assertTrue(reader.readRecord());

        Assert.assertEquals("Chicane", reader.get(0));

        Assert.assertEquals("Love on the Run", reader.get(1));

        Assert.assertEquals("Knight Rider", reader.get(2));

        Assert.assertEquals("This field contains a comma, but it doesn't matter as the field is quoted", reader.get(3));

        Assert.assertEquals(0L, reader.getCurrentRecord());

        Assert.assertEquals(4, reader.getColumnCount());

        Assert.assertEquals("", reader.getRawRecord());

        Assert.assertFalse(reader.getCaptureRawRecord());

        reader.setCaptureRawRecord(true);

        Assert.assertTrue(reader.getCaptureRawRecord());

        Assert.assertTrue(reader.readRecord());

        Assert.assertEquals("\"Samuel Barber\", \"Adagio for Strings\", \"Classical\", \"This field contains a double quote character, \"\", but it doesn't matter as it is escaped\"", reader.getRawRecord());

        Assert.assertEquals("Samuel Barber", reader.get(0));

        Assert.assertEquals("Adagio for Strings", reader.get(1));

        Assert.assertEquals("Classical", reader.get(2));

        Assert.assertEquals("This field contains a double quote character, \", but it doesn't matter as it is escaped", reader.get(3));

        Assert.assertEquals(1L, reader.getCurrentRecord());

        Assert.assertEquals(4, reader.getColumnCount());

        Assert.assertFalse(reader.readRecord());

        Assert.assertTrue(reader.getCaptureRawRecord());

        Assert.assertEquals("", reader.getRawRecord());

        reader.close();

    }
