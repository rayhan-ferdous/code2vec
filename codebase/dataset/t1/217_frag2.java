    private Pool loadMixtureWeightsBinary(String path, float floor) throws FileNotFoundException, IOException {

        logger.fine("Loading mixture weights from: " + path);

        int numStates;

        int numStreams;

        int numGaussiansPerState;

        int numValues;

        Properties props = new Properties();

        DataInputStream dis = readS3BinaryHeader(location, path, props);

        String version = props.getProperty("version");

        boolean doCheckSum;

        if (version == null || !version.equals(MIXW_FILE_VERSION)) {

            throw new IOException("Unsupported version in " + path);

        }

        String checksum = props.getProperty("chksum0");

        doCheckSum = (checksum != null && checksum.equals("yes"));

        Pool pool = new Pool(path);

        numStates = readInt(dis);

        numStreams = readInt(dis);

        numGaussiansPerState = readInt(dis);

        numValues = readInt(dis);

        assert numValues == numStates * numStreams * numGaussiansPerState;

        assert numStreams == 1;

        pool.setFeature(NUM_SENONES, numStates);

        pool.setFeature(NUM_STREAMS, numStreams);

        pool.setFeature(NUM_GAUSSIANS_PER_STATE, numGaussiansPerState);

        for (int i = 0; i < numStates; i++) {

            float[] logMixtureWeight = readFloatArray(dis, numGaussiansPerState);
