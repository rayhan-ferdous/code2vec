    private void buildSqlOutput(PrintWriter fout, String dumpPath, int tmpFileNumber) throws Exception {

        String line;

        String fileName = dumpPath + File.separator + tmpFileNumber;

        BufferedReader rdr = new BufferedReader(new FileReader(fileName));

        while ((line = rdr.readLine()) != null) {

            fout.println(line);

        }

        rdr.close();

        new File(fileName).delete();

    }
