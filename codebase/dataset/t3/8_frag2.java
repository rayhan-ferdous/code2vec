    public void test114() throws Exception {

        try {

            CsvWriter writer = new CsvWriter((Writer) null, ',');

        } catch (Exception ex) {

            assertException(new IllegalArgumentException("Parameter outputStream can not be null."), ex);

        }

    }
