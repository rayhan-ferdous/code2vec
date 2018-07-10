    public void logInfo() {

        logger.info("ModelLoader");

        meansPool.logInfo(logger);

        variancePool.logInfo(logger);

        matrixPool.logInfo(logger);

        senonePool.logInfo(logger);

        meanTransformationMatrixPool.logInfo(logger);

        meanTransformationVectorPool.logInfo(logger);

        varianceTransformationMatrixPool.logInfo(logger);

        varianceTransformationVectorPool.logInfo(logger);

        mixtureWeightsPool.logInfo(logger);

        senonePool.logInfo(logger);

        logger.info("Context Independent Unit Entries: " + contextIndependentUnits.size());

        hmmManager.logInfo(logger);

    }
