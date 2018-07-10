        buffer = stream.toByteArray();

        stream.close();

        String data = Charset.forName("ISO-8859-1").decode(ByteBuffer.wrap(buffer)).toString();

        Assert.assertEquals("1,2\t3\t\"blah \"\"some stuff in quotes\"\"\"\r\n", data);

    }


