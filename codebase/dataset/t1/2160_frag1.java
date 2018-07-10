    private Pool createDummyVectorPool(String name, int card) {

        logger.fine("creating dummy vector pool " + name + " " + card);

        Pool pool = new Pool(name);

        for (int classe = 0; classe < card; classe++) {

            float[] vector = new float[vectorLength];

            for (int i = 0; i < vectorLength; i++) {

                vector[i] = 0.0f;

            }

            pool.put(classe, vector);

        }

        return pool;

    }
