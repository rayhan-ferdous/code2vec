    private Pool loadDensityFileAscii(String path, float floor) throws FileNotFoundException, IOException {

        int token_type;

        int numStates;

        int numStreams;

        int numGaussiansPerState;

        InputStream inputStream = getClass().getResourceAsStream(path);

        if (inputStream == null) {

            throw new FileNotFoundException("Error trying to read file " + location + path);

        }

        ExtendedStreamTokenizer est = new ExtendedStreamTokenizer(inputStream, '#', false);

        Pool pool = new Pool(path);

        logger.fine("Loading density file from: " + path);

        est.expectString("param");

        numStates = est.getInt("numStates");

        numStreams = est.getInt("numStreams");

        numGaussiansPerState = est.getInt("numGaussiansPerState");

        pool.setFeature(NUM_SENONES, numStates);

        pool.setFeature(NUM_STREAMS, numStreams);

        pool.setFeature(NUM_GAUSSIANS_PER_STATE, numGaussiansPerState);

        for (int i = 0; i < numStates; i++) {

            est.expectString("mgau");

            est.expectInt("mgau index", i);

            est.expectString("feat");

            est.expectInt("feat index", 0);

            for (int j = 0; j < numGaussiansPerState; j++) {

                est.expectString("density");

                est.expectInt("densityValue", j);

                float[] density = new float[vectorLength];

                for (int k = 0; k < vectorLength; k++) {

                    density[k] = est.getFloat("val");

                    if (density[k] < floor) {

                        density[k] = floor;

                    }

                }

                int id = i * numGaussiansPerState + j;

                pool.put(id, density);

            }

        }

        est.close();

        return pool;

    }
