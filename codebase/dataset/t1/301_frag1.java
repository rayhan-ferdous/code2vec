    protected float readFloat(DataInputStream dis) throws IOException {

        float val;

        if (swap) {

            val = Utilities.readLittleEndianFloat(dis);

        } else {

            val = dis.readFloat();

        }

        return val;

    }



    /**

     * If a data point is non-zero and below 'floor' make it equal to floor

     * (don't floor zero values though).

     * 

     * @param data

     *                the data to floor

     * @param floor

     *                the floored value

     */

    protected void nonZeroFloor(float[] data, float floor) {

        for (int i = 0; i < data.length; i++) {

            if (data[i] != 0.0 && data[i] < floor) {

                data[i] = floor;

            }

        }

    }



    /**

     * If a data point is below 'floor' make it equal to floor.

     * 

     * @param data

     *                the data to floor

     * @param floor

     *                the floored value

     */

    private void floorData(float[] data, float floor) {

        for (int i = 0; i < data.length; i++) {

            if (data[i] < floor) {

                data[i] = floor;

            }

        }

    }



    /**

     * Normalize the given data

     * 

     * @param data

     *                the data to normalize

     */

    protected void normalize(float[] data) {

        float sum = 0;

        for (int i = 0; i < data.length; i++) {

            sum += data[i];

        }

        if (sum != 0.0f) {

            for (int i = 0; i < data.length; i++) {

                data[i] = data[i] / sum;

            }

        }

    }



    /**

     * Dump the data

     * 

     * @param name

     *                the name of the data

     * @param data

     *                the data itself

     *  

     */

    private void dumpData(String name, float[] data) {

        System.out.println(" ----- " + name + " -----------");

        for (int i = 0; i < data.length; i++) {

            System.out.println(name + " " + i + ": " + data[i]);

        }

    }



    protected void convertToLogMath(float[] data) {

        for (int i = 0; i < data.length; i++) {

            data[i] = logMath.linearToLog(data[i]);

        }

    }



    /**

     * Reads the given number of floats from the stream and returns them in an

     * array of floats

     * 

     * @param dis

     *                the stream to read data from

     * @param size

     *                the number of floats to read

     * 

     * @return an array of size float elements

     * 

     * @throws IOException

     *                 if an exception occurs

     */

    protected float[] readFloatArray(DataInputStream dis, int size) throws IOException {

        float[] data = new float[size];

        for (int i = 0; i < size; i++) {

            data[i] = readFloat(dis);

        }

        return data;

    }



    /**

     * Loads the sphinx3 densityfile, a set of density arrays are created and

     * placed in the given pool.

     * 

     * @param useCDUnits

     *                if true, loads also the context dependent units

     * @param inputStream

     *                the open input stream to use

     * @param path

     *                the path to a density file

     * 

     * @return a pool of loaded densities

     * 

     * @throws FileNotFoundException

     *                 if a file cannot be found

     * @throws IOException

     *                 if an error occurs while loading the data

     */

    protected Pool loadHMMPool(boolean useCDUnits, InputStream inputStream, String path) throws FileNotFoundException, IOException {

        int token_type;

        int numBase;

        int numTri;

        int numStateMap;

        int numTiedState;

        int numStatePerHMM;

        int numContextIndependentTiedState;

        int numTiedTransitionMatrices;

        ExtendedStreamTokenizer est = new ExtendedStreamTokenizer(inputStream, '#', false);

        Pool pool = new Pool(path);

        logger.fine("Loading HMM file from: " + path);

        est.expectString(MODEL_VERSION);

        numBase = est.getInt("numBase");

        est.expectString("n_base");

        numTri = est.getInt("numTri");

        est.expectString("n_tri");

        numStateMap = est.getInt("numStateMap");

        est.expectString("n_state_map");

        numTiedState = est.getInt("numTiedState");

        est.expectString("n_tied_state");

        numContextIndependentTiedState = est.getInt("numContextIndependentTiedState");

        est.expectString("n_tied_ci_state");

        numTiedTransitionMatrices = est.getInt("numTiedTransitionMatrices");

        est.expectString("n_tied_tmat");

        numStatePerHMM = numStateMap / (numTri + numBase);

        assert numTiedState == mixtureWeightsPool.getFeature(NUM_SENONES, 0);

        assert numTiedTransitionMatrices == matrixPool.size();

        for (int i = 0; i < numBase; i++) {

            String name = est.getString();

            String left = est.getString();

            String right = est.getString();

            String position = est.getString();

            String attribute = est.getString();

            int tmat = est.getInt("tmat");

            int[] stid = new int[numStatePerHMM - 1];

            for (int j = 0; j < numStatePerHMM - 1; j++) {

                stid[j] = est.getInt("j");

                assert stid[j] >= 0 && stid[j] < numContextIndependentTiedState;

            }

            est.expectString("N");

            assert left.equals("-");

            assert right.equals("-");

            assert position.equals("-");

            assert tmat < numTiedTransitionMatrices;

            Unit unit = unitManager.getUnit(name, attribute.equals(FILLER));

            contextIndependentUnits.put(unit.getName(), unit);

            if (logger.isLoggable(Level.FINE)) {

                logger.fine("Loaded " + unit);

            }

            if (unit.isFiller() && unit.getName().equals(SILENCE_CIPHONE)) {

                unit = UnitManager.SILENCE;

            }

            float[][] transitionMatrix = (float[][]) matrixPool.get(tmat);

            SenoneSequence ss = getSenoneSequence(stid);

            HMM hmm = new SenoneHMM(unit, ss, transitionMatrix, HMMPosition.lookup(position));

            hmmManager.put(hmm);

        }

        String lastUnitName = "";

        Unit lastUnit = null;

        int[] lastStid = null;

        SenoneSequence lastSenoneSequence = null;

        for (int i = 0; i < numTri; i++) {

            String name = est.getString();

            String left = est.getString();

            String right = est.getString();

            String position = est.getString();

            String attribute = est.getString();

            int tmat = est.getInt("tmat");

            int[] stid = new int[numStatePerHMM - 1];

            for (int j = 0; j < numStatePerHMM - 1; j++) {

                stid[j] = est.getInt("j");

                assert stid[j] >= numContextIndependentTiedState && stid[j] < numTiedState;

            }

            est.expectString("N");

            assert !left.equals("-");

            assert !right.equals("-");

            assert !position.equals("-");

            assert attribute.equals("n/a");

            assert tmat < numTiedTransitionMatrices;

            if (useCDUnits) {

                Unit unit = null;

                String unitName = (name + " " + left + " " + right);

                if (unitName.equals(lastUnitName)) {

                    unit = lastUnit;

                } else {

                    Unit[] leftContext = new Unit[1];

                    leftContext[0] = (Unit) contextIndependentUnits.get(left);

                    Unit[] rightContext = new Unit[1];

                    rightContext[0] = (Unit) contextIndependentUnits.get(right);

                    Context context = LeftRightContext.get(leftContext, rightContext);

                    unit = unitManager.getUnit(name, false, context);

                }

                lastUnitName = unitName;

                lastUnit = unit;

                if (logger.isLoggable(Level.FINE)) {

                    logger.fine("Loaded " + unit);

                }

                float[][] transitionMatrix = (float[][]) matrixPool.get(tmat);

                SenoneSequence ss = lastSenoneSequence;

                if (ss == null || !sameSenoneSequence(stid, lastStid)) {

                    ss = getSenoneSequence(stid);

                }

                lastSenoneSequence = ss;

                lastStid = stid;

                HMM hmm = new SenoneHMM(unit, ss, transitionMatrix, HMMPosition.lookup(position));

                hmmManager.put(hmm);

            }

        }

        est.close();

        return pool;

    }



    /**

          * Returns true if the given senone sequence IDs are the same.

          * 

          * @return true if the given senone sequence IDs are the same, false

          *         otherwise

          */

    protected boolean sameSenoneSequence(int[] ssid1, int[] ssid2) {

        if (ssid1.length == ssid2.length) {

            for (int i = 0; i < ssid1.length; i++) {

                if (ssid1[i] != ssid2[i]) {

                    return false;

                }

            }

            return true;

        } else {

            return false;

        }

    }



    /**

     * Gets the senone sequence representing the given senones

     * 

     * @param stateid

     *                is the array of senone state ids

     * 

     * @return the senone sequence associated with the states

     */

    protected SenoneSequence getSenoneSequence(int[] stateid) {

        Senone[] senones = new Senone[stateid.length];

        for (int i = 0; i < stateid.length; i++) {

            senones[i] = (Senone) senonePool.get(stateid[i]);

        }

        return new SenoneSequence(senones);

    }



    /**

     * Loads the mixture weights

     * 

     * @param path

     *                the path to the mixture weight file

     * @param floor

     *                the minimum mixture weight allowed

     * 

     * @return a pool of mixture weights

     * 

     * @throws FileNotFoundException

     *                 if a file cannot be found

     * @throws IOException

     *                 if an error occurs while loading the data

     */

    private Pool loadMixtureWeightsAscii(String path, float floor) throws FileNotFoundException, IOException {

        logger.fine("Loading mixture weights from: " + path);

        int numStates;

        int numStreams;

        int numGaussiansPerState;

        InputStream inputStream = StreamFactory.getInputStream(location, path);

        Pool pool = new Pool(path);

        ExtendedStreamTokenizer est = new ExtendedStreamTokenizer(inputStream, '#', false);

        est.expectString("mixw");

        numStates = est.getInt("numStates");

        numStreams = est.getInt("numStreams");

        numGaussiansPerState = est.getInt("numGaussiansPerState");

        pool.setFeature(NUM_SENONES, numStates);

        pool.setFeature(NUM_STREAMS, numStreams);

        pool.setFeature(NUM_GAUSSIANS_PER_STATE, numGaussiansPerState);

        for (int i = 0; i < numStates; i++) {

            est.expectString("mixw");

            est.expectString("[" + i);

            est.expectString("0]");

            float total = est.getFloat("total");

            float[] logMixtureWeight = new float[numGaussiansPerState];

            for (int j = 0; j < numGaussiansPerState; j++) {

                float val = est.getFloat("mixwVal");

                if (val < floor) {

                    val = floor;

                }

                logMixtureWeight[j] = val;

            }

            convertToLogMath(logMixtureWeight);

            pool.put(i, logMixtureWeight);

        }

        est.close();

        return pool;

    }



    /**

     * Loads the mixture weights (Binary)

     * 

     * @param path

     *                the path to the mixture weight file

     * @param floor

     *                the minimum mixture weight allowed

     * 

     * @return a pool of mixture weights

     * 

     * @throws FileNotFoundException

     *                 if a file cannot be found

     * @throws IOException

     *                 if an error occurs while loading the data

     */

    private Pool loadMixtureWeightsBinary(String path, float floor) throws FileNotFoundException, IOException {
