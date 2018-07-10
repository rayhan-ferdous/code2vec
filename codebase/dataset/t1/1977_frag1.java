        writer.write("3");

        writer.write("blah \"some stuff in quotes\"");

        writer.close();

        buffer = stream.toByteArray();

        stream.close();

        String data = Charset.forName("ISO-8859-1").decode(ByteBuffer.wrap(buffer)).toString();

        Assert.assertEquals("\"1,2\",3,\"blah \"\"some stuff in quotes\"\"\"\r\n\"1,2\",\"3\",\"blah \"\"some stuff in quotes\"\"\"", data);

    }


