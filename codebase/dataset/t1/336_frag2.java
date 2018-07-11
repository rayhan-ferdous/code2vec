    @Test

    public void test147() throws Exception {

        StringBuilder data = new StringBuilder(20000);

        for (int i = 0; i < 10000; i++) {

            data.append("\\b");

        }

        CsvReader reader = CsvReader.parse(data.toString());

        reader.setUseTextQualifier(false);

        reader.setEscapeMode(CsvReader.ESCAPE_MODE_BACKSLASH);

        Assert.assertTrue(reader.readRecord());

        Assert.assertEquals(generateString('\b', 10000), reader.get(0));

        Assert.assertFalse(reader.readRecord());

        reader.close();

    }
