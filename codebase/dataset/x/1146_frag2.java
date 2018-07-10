    public void test119() throws Exception {

        byte[] buffer;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        CsvWriter writer = new CsvWriter(stream, ',', Charset.forName("ISO-8859-1"));

        writer.write("1,2");

        writer.write("3");

        writer.endRecord();

        Assert.assertEquals(',', writer.getDelimiter());

        writer.setDelimiter('\t');

        Assert.assertEquals('\t', writer.getDelimiter());

        writer.write("1,2");

        writer.write("3");

        writer.endRecord();

        writer.close();

        buffer = stream.toByteArray();

        stream.close();

        String data = Charset.forName("ISO-8859-1").decode(ByteBuffer.wrap(buffer)).toString();

        Assert.assertEquals("\"1,2\",3\r\n1,2\t3\r\n", data);

    }
