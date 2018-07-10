    public Pool getVariancePool() {

        return variancePool;

    }



    /**

     * Gets the variance transformation matrix pool

     * 

     * @return the pool

     */

    public Pool getVarianceTransformationMatrixPool() {

        return varianceTransformationMatrixPool;

    }



    /**

     * Gets the pool of variance transformation vectors for this loader

     * 

     * @return the pool

     */

    public Pool getVarianceTransformationVectorPool() {

        return varianceTransformationVectorPool;

    }



    public Pool getMixtureWeightPool() {

        return mixtureWeightsPool;

    }



    public Pool getTransitionMatrixPool() {

        return matrixPool;

    }



    public Pool getSenonePool() {

        return senonePool;

    }



    /**

     * Returns the size of the left context for context dependent units

     * 

     * @return the left context size

     */

    public int getLeftContextSize() {

        return CONTEXT_SIZE;
