        } catch (Exception ex) {

            assertException(new IllegalArgumentException("Parameter inputStream can not be null."), ex);

        }

    }



    @Test

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



    @Test

    public void test92() throws Exception {

        byte[] buffer;

        String test = "test";

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        PrintWriter writer = new PrintWriter(stream);

        writer.println(test);

        writer.close();

        buffer = stream.toByteArray();

        stream.close();

        CsvReader reader = new CsvReader(new ByteArrayInputStream(buffer), ',', Charset.forName("ISO-8859-1"));

        reader.readRecord();

        Assert.assertEquals(test, reader.get(0));

        reader.close();

    }



    @Test

    public void test112() throws Exception {

        try {
