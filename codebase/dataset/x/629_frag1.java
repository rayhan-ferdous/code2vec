        } catch (SQLException ex) {

            throw new HamboFatalException(MSG_SQL_FAILED, ex);

        } finally {

            if (con != null) {

                con.release();

            }

        }

        return user;

    }



    /**

     * Will attempt to find a stored user entry with the given user id.

     * @param userId the wanted user's user id.

     * @return a {@link hambo.user.User} object if found, otherwise null.

     */

    public User findUser(String userId) throws HamboFatalException {

        DBConnection con = null;

        User user = null;

        try {

            con = DBServiceManager.allocateConnection();

            String sql = "select " + TABLE_COLUMNS + "  from user_UserAccount where userid=?";
