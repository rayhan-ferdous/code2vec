    @Test

    public void test84() throws Exception {

        StringBuilder holder = new StringBuilder(200010);

        for (int i = 0; i < 100000; i++) {

            holder.append("a,");

        }

        holder.append("a");

        CsvReader reader = CsvReader.parse(holder.toString());

        reader.setSafetySwitch(false);

        reader.readRecord();

        reader.close();

    }
