    @Test

    public void test131() throws Exception {

        byte[] buffer;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        CsvWriter writer = new CsvWriter(stream, ',', Charset.forName("ISO-8859-1"));

        writer.setUseTextQualifier(false);

        writer.setEscapeMode(CsvWriter.ESCAPE_MODE_BACKSLASH);

        writer.write("1,\\\r\n2");

        writer.endRecord();

        writer.setRecordDelimiter(';');

        writer.write("1,\\;2");

        writer.endRecord();

        writer.close();

        buffer = stream.toByteArray();

        stream.close();

        String data = Charset.forName("ISO-8859-1").decode(ByteBuffer.wrap(buffer)).toString();

        Assert.assertEquals("1\\,\\\\\\\r\\\n2\r\n1\\,\\\\\\;2;", data);

    }
