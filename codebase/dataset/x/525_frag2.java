            convertToLogMath(logMixtureWeight);

            pool.put(i, logMixtureWeight);

        }

        dis.close();

        return pool;

    }



    /**

          * Loads the transition matrices

          * 

          * @param path

          *                the path to the transitions matrices

          * 

          * @return a pool of transition matrices

          * 

          * @throws FileNotFoundException

          *                 if a file cannot be found

          * @throws IOException

          *                 if an error occurs while loading the data

          */

    protected Pool loadTransitionMatricesAscii(String path) throws FileNotFoundException, IOException {

        InputStream inputStream = StreamFactory.getInputStream(location, path);

        logger.fine("Loading transition matrices from: " + path);

        int numMatrices;

        int numStates;

        Pool pool = new Pool(path);

        ExtendedStreamTokenizer est = new ExtendedStreamTokenizer(inputStream, '#', false);

        est.expectString("tmat");

        numMatrices = est.getInt("numMatrices");

        numStates = est.getInt("numStates");

        logger.fine("with " + numMatrices + " and " + numStates + " states, in " + (sparseForm ? "sparse" : "dense") + " form");

        for (int i = 0; i < numMatrices; i++) {

            est.expectString("tmat");

            est.expectString("[" + i + "]");

            float[][] tmat = new float[numStates][numStates];

            for (int j = 0; j < numStates; j++) {

                for (int k = 0; k < numStates; k++) {

                    if (j < numStates - 1) {

                        if (sparseForm) {

                            if (k == j || k == j + 1) {

                                tmat[j][k] = est.getFloat("tmat value");

                            }

                        } else {

                            tmat[j][k] = est.getFloat("tmat value");

                        }

                    }

                    tmat[j][k] = logMath.linearToLog(tmat[j][k]);

                    if (logger.isLoggable(Level.FINE)) {

                        logger.fine("tmat j " + j + " k " + k + " tm " + tmat[j][k]);

                    }

                }

            }

            pool.put(i, tmat);

        }

        est.close();

        return pool;

    }



    /**

     * Loads the transition matrices (Binary)

     * 

     * @param path

     *                the path to the transitions matrices

     * 

     * @return a pool of transition matrices

     * 

     * @throws FileNotFoundException

     *                 if a file cannot be found

     * @throws IOException

     *                 if an error occurs while loading the data

     */

    protected Pool loadTransitionMatricesBinary(String path) throws FileNotFoundException, IOException {

        logger.fine("Loading transition matrices from: " + path);

        int numMatrices;

        int numStates;

        int numRows;

        int numValues;

        Properties props = new Properties();

        DataInputStream dis = readS3BinaryHeader(location, path, props);

        String version = props.getProperty("version");

        boolean doCheckSum;

        if (version == null || !version.equals(TMAT_FILE_VERSION)) {

            throw new IOException("Unsupported version in " + path);

        }

        String checksum = props.getProperty("chksum0");

        doCheckSum = (checksum != null && checksum.equals("yes"));

        Pool pool = new Pool(path);

        numMatrices = readInt(dis);

        numRows = readInt(dis);

        numStates = readInt(dis);

        numValues = readInt(dis);

        assert numValues == numStates * numRows * numMatrices;

        for (int i = 0; i < numMatrices; i++) {

            float[][] tmat = new float[numStates][];

            tmat[numStates - 1] = new float[numStates];

            convertToLogMath(tmat[numStates - 1]);

            for (int j = 0; j < numRows; j++) {

                tmat[j] = readFloatArray(dis, numStates);
