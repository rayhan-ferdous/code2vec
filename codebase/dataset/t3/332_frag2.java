    public void test58() throws Exception {

        byte[] buffer;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        CsvWriter writer = new CsvWriter(stream, '\t', Charset.forName("ISO-8859-1"));

        writer.write("data\r\nmore data");

        writer.write(" 3\t", false);

        writer.write(" 3\t");

        writer.write(" 3\t", true);

        writer.endRecord();

        writer.close();

        buffer = stream.toByteArray();

        stream.close();

        String data = Charset.forName("ISO-8859-1").decode(ByteBuffer.wrap(buffer)).toString();

        Assert.assertEquals("\"data\r\nmore data\"\t3\t3\t\" 3\t\"\r\n", data);

    }
