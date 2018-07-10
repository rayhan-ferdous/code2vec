        log.debug("Getting Result");

        Integer fileID = (Integer) q.list().get(0);

        log.debug("Returning Staging ID: " + fileID);

        return fileID;

    }



    /**

	 * Adds an audit record.

	 * Should be called only once at the start of a new message.

	 * 

	 * @param messageid

	 * @param message

	 * @throws Exception

	 */

    public static void setAuditRecord(String messageid, String message) throws Exception {

        log.debug("Adding audit  message");

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
