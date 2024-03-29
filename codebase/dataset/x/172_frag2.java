    @Override

    public Taxonomy call() throws Exception {

        progressStream = new ProgressInputStream(streamProvider.getInput());

        BufferedReader reader = new BufferedReader(new InputStreamReader(progressStream, Charset.forName("UTF-8")));

        try {

            return (new XmlTaxonImport()).importTaxa(reader);

        } finally {

            reader.close();

        }

    }
