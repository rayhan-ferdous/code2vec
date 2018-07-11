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
