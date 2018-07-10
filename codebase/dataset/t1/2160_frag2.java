    private Pool createDummyVectorPool(String name) {

        logger.fine("creating dummy vector pool " + name);

        Pool pool = new Pool(name);

        float[] vector = new float[vectorLength];

        for (int i = 0; i < vectorLength; i++) {

            vector[i] = 0.0f;

        }

        pool.put(0, vector);

        return pool;

    }
