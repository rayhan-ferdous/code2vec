    private Pool<Senone> createSenonePool(float distFloor, float varianceFloor) {

        Pool<Senone> pool = new Pool<Senone>("senones");

        int numMixtureWeights = mixtureWeightsPool.size();

        int numMeans = meansPool.size();

        int numVariances = variancePool.size();

        int numGaussiansPerSenone = mixtureWeightsPool.getFeature(NUM_GAUSSIANS_PER_STATE, 0);

        int numSenones = mixtureWeightsPool.getFeature(NUM_SENONES, 0);

        int whichGaussian = 0;

        logger.fine("NG " + numGaussiansPerSenone);

        logger.fine("NS " + numSenones);

        logger.fine("NMIX " + numMixtureWeights);

        logger.fine("NMNS " + numMeans);

        logger.fine("NMNS " + numVariances);

        assert numGaussiansPerSenone > 0;

        assert numMixtureWeights == numSenones;

        assert numVariances == numSenones * numGaussiansPerSenone;

        assert numMeans == numSenones * numGaussiansPerSenone;

        float[][] meansTransformationMatrix = meanTransformationMatrixPool == null ? null : meanTransformationMatrixPool.get(0);

        float[] meansTransformationVector = meanTransformationVectorPool == null ? null : meanTransformationVectorPool.get(0);

        float[][] varianceTransformationMatrix = varianceTransformationMatrixPool == null ? null : varianceTransformationMatrixPool.get(0);

        float[] varianceTransformationVector = varianceTransformationVectorPool == null ? null : varianceTransformationVectorPool.get(0);

        for (int i = 0; i < numSenones; i++) {

            MixtureComponent[] mixtureComponents = new MixtureComponent[numGaussiansPerSenone];

            for (int j = 0; j < numGaussiansPerSenone; j++) {

                mixtureComponents[j] = new MixtureComponent(logMath, meansPool.get(whichGaussian), meansTransformationMatrix, meansTransformationVector, variancePool.get(whichGaussian), varianceTransformationMatrix, varianceTransformationVector, distFloor, varianceFloor);

                whichGaussian++;

            }

            Senone senone = new GaussianMixture(logMath, mixtureWeightsPool.get(i), mixtureComponents, i);

            pool.put(i, senone);

        }

        return pool;

    }
