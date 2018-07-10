        for (int ii = 0; ii < repositories.length; ii++) {

            l_repos.append(repositories[ii]).append(',');

        }

        throw new RepositoryException("None of the repositories [" + l_repos.toString() + "] contained the metadata properties for " + artifact, l_throwable);

    }



    /**

     * Gets the Properties in a remote properties file.

     * 

     * @param url the url to the properties file

     * @return the loaded properties for the file

     * @throws IOException indicating a IO error during property loading

     */

    public static Properties getProperties(URL url) throws IOException {

        InputStream l_in = null;

        Properties l_props = new Properties();
