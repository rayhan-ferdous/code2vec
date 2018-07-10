        return getAsAttributes(getProperties(cache, artifact));

    }



    /**

     * Gets the Properties in the local cache.

     * 

     * @param cache the local cache

     * @param artifact the artifact to load meta data from

     * @return the loaded properties 

     * @throws RepositoryException if there is any problem loading the 

     *    properties

     */

    public static Properties getProperties(File cache, Artifact artifact) throws RepositoryException {

        File local = new File(cache, artifact.getPath() + "." + META);

        if (!local.exists()) {

            final String error = "Cannot load metadata due to missing resurce.";

            Throwable cause = new FileNotFoundException(local.toString());
