    @Test

    public void test124() throws Exception {

        byte[] buffer;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        CsvWriter writer = new CsvWriter(stream, ',', Charset.forName("ISO-8859-1"));

        writer.setRecordDelimiter(';');

        writer.setUseTextQualifier(false);

        writer.setEscapeMode(CsvWriter.ESCAPE_MODE_BACKSLASH);

        writer.write("1;2");

        writer.endRecord();

        writer.close();

        buffer = stream.toByteArray();

        stream.close();

        String data = Charset.forName("ISO-8859-1").decode(ByteBuffer.wrap(buffer)).toString();

        Assert.assertEquals("1\\;2;", data);

    }
