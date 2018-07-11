    public RevisionFeed getRevisionsFeed(String resourceId) throws Exception {

        if (resourceId == null) {

            throw new RuntimeException("null resourceId");

        }

        URL url = buildUrl(URL_DEFAULT + URL_DOCLIST_FEED + "/" + resourceId + URL_REVISIONS);

        return service.getFeed(url, RevisionFeed.class);

    }



    /**

	 * Search the documents, and return a feed of docs that match.

	 * 

	 * @param searchParameters parameters to be used in searching criteria.

	 * 

	 * @throws IOException

	 * @throws MalformedURLException

	 * @throws ServiceException

	 * @throws RuntimeException

	 */

    public DocumentListFeed search(Map<String, String> searchParameters) throws Exception {
