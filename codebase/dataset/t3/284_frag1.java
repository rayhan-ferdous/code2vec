    public void test91() throws Exception {

        byte[] buffer;

        String test = "test";

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        PrintWriter writer = new PrintWriter(stream);

        writer.println(test);

        writer.close();

        buffer = stream.toByteArray();

        stream.close();

        CsvReader reader = new CsvReader(new ByteArrayInputStream(buffer), Charset.forName("ISO-8859-1"));

        reader.readRecord();

        Assert.assertEquals(test, reader.get(0));

        reader.close();

    }
