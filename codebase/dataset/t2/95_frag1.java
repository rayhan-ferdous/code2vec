    public AclFeed getAclFeed(String resourceId) throws Exception {

        if (resourceId == null) {

            throw new DocumentListException("null resourceId");

        }

        URL url = buildUrl(URL_DEFAULT + URL_DOCLIST_FEED + "/" + resourceId + URL_ACL);

        return service.getFeed(url, AclFeed.class);

    }



    /**

	 * Add an ACL role to an object.

	 * 

	 * @param role the role of the ACL to be added to the object.

	 * @param scope the scope for the ACL.

	 * @param resourceId the resource id of the object to set the ACL for.

	 * 

	 * @throws IOException

	 * @throws MalformedURLException

	 * @throws ServiceException

	 * @throws DocumentListException

	 */

    public AclEntry addAclRole(AclRole role, AclScope scope, String resourceId) throws Exception {
