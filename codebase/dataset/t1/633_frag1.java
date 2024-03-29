        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        CsvWriter writer = new CsvWriter(stream, ',', Charset.forName("ISO-8859-1"));

        writer.setEscapeMode(CsvWriter.ESCAPE_MODE_BACKSLASH);

        writer.setUseTextQualifier(false);

        writer.write("1,2");

        writer.write("3");

        writer.write("blah \"some stuff in quotes\"");

        writer.endRecord();

        writer.close();

        buffer = stream.toByteArray();

        stream.close();

        String data = Charset.forName("ISO-8859-1").decode(ByteBuffer.wrap(buffer)).toString();

        Assert.assertEquals("1\\,2,3,blah \"some stuff in quotes\"\r\n", data);

    }


