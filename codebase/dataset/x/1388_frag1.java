    @Override

    public void readFrom(DataInputStream in) throws IOException, IllegalAccessException, InstantiationException {

        super.readFrom(in);

        int numberOfSinks = sinks.size();

        this.reader = null;

        this.readerForFaucets = new ArrayList<PipedReader>(numberOfSinks);

        this.writers = new ArrayList<PipedWriter>(numberOfSinks);

        for (int i = 0; i < numberOfSinks; i++) {

            this.readerForFaucets.add(null);

            this.writers.add(null);

        }

    }
