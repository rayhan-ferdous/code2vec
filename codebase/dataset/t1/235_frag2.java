    private Pool createDummyMatrixPool(String name) {

        Pool pool = new Pool(name);

        float[][] matrix = new float[vectorLength][vectorLength];

        logger.fine("creating dummy matrix pool " + name);

        for (int i = 0; i < vectorLength; i++) {

            for (int j = 0; j < vectorLength; j++) {

                if (i == j) {

                    matrix[i][j] = 1.0F;

                } else {

                    matrix[i][j] = 0.0F;

                }

            }

        }

        pool.put(0, matrix);

        return pool;

    }
