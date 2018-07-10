    public void test31() throws Exception {

        CsvWriter writer = new CsvWriter(new PrintWriter(new OutputStreamWriter(new FileOutputStream("temp.csv"), Charset.forName("UTF-8"))), ',');

        writer.write(" \t \t");

        writer.close();

        CsvReader reader = new CsvReader(new InputStreamReader(new FileInputStream("temp.csv"), Charset.forName("UTF-8")));

        Assert.assertTrue(reader.readRecord());

        Assert.assertEquals("", reader.get(0));

        Assert.assertEquals(1, reader.getColumnCount());

        Assert.assertEquals(0L, reader.getCurrentRecord());

        Assert.assertEquals("\"\"", reader.getRawRecord());

        Assert.assertFalse(reader.readRecord());

        reader.close();

        new File("temp.csv").delete();

    }
