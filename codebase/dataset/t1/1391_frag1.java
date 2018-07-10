        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        CsvWriter writer = new CsvWriter(stream, ',', Charset.forName("ISO-8859-1"));

        Assert.assertEquals('#', writer.getComment());

        writer.setComment('~');

        Assert.assertEquals('~', writer.getComment());

        writer.setRecordDelimiter(';');

        writer.write("1");

        writer.endRecord();

        writer.writeComment("blah");

        writer.close();

        buffer = stream.toByteArray();

        stream.close();

        String data = Charset.forName("ISO-8859-1").decode(ByteBuffer.wrap(buffer)).toString();

        Assert.assertEquals("1;~blah;", data);

    }



    @Test
