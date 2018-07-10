        writer.close();

        buffer = stream.toByteArray();

        stream.close();

        String data = Charset.forName("ISO-8859-1").decode(ByteBuffer.wrap(buffer)).toString();

        Assert.assertEquals("\"data\r\nmore data\"\t3\t3\t\" 3\t\"\r\n", data);

    }


