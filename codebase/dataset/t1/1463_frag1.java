    private Pool loadDensityFileBinary(String path, float floor) throws FileNotFoundException, IOException {

        int token_type;

        int numStates;

        int numStreams;

        int numGaussiansPerState;

        Properties props = new Properties();

        int blockSize = 0;

        DataInputStream dis = readS3BinaryHeader(location, path, props);

        String version = props.getProperty("version");

        boolean doCheckSum;

        if (version == null || !version.equals(DENSITY_FILE_VERSION)) {

            throw new IOException("Unsupported version in " + path);

        }

        String checksum = props.getProperty("chksum0");

        doCheckSum = (checksum != null && checksum.equals("yes"));

        numStates = readInt(dis);

        numStreams = readInt(dis);

        numGaussiansPerState = readInt(dis);

        int[] vectorLength = new int[numStreams];

        for (int i = 0; i < numStreams; i++) {

            vectorLength[i] = readInt(dis);

        }

        int rawLength = readInt(dis);

        logger.fine("Nstates " + numStates);

        logger.fine("Nstreams " + numStreams);

        logger.fine("NgaussiansPerState " + numGaussiansPerState);

        logger.fine("vectorLength " + vectorLength.length);

        logger.fine("rawLength " + rawLength);

        for (int i = 0; i < numStreams; i++) {

            blockSize += vectorLength[i];

        }

        assert rawLength == numGaussiansPerState * blockSize * numStates;

        assert numStreams == 1;

        Pool pool = new Pool(path);

        pool.setFeature(NUM_SENONES, numStates);

        pool.setFeature(NUM_STREAMS, numStreams);

        pool.setFeature(NUM_GAUSSIANS_PER_STATE, numGaussiansPerState);

        int r = 0;

        for (int i = 0; i < numStates; i++) {

            for (int j = 0; j < numStreams; j++) {

                for (int k = 0; k < numGaussiansPerState; k++) {

                    float[] density = readFloatArray(dis, vectorLength[j]);

                    floorData(density, floor);

                    pool.put(i * numGaussiansPerState + k, density);

                }

            }

        }

        int checkSum = readInt(dis);

        dis.close();

        return pool;

    }
