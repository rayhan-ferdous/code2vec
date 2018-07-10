    @Test

    public void test113() throws Exception {

        try {

            CsvWriter writer = new CsvWriter("test.csv", ',', (Charset) null);

        } catch (Exception ex) {

            assertException(new IllegalArgumentException("Parameter charset can not be null."), ex);

        }

    }
