    public void free() throws SQLException {

        try {

            close();

        } catch (IOException ex) {

            throw new FBSQLException(ex);

        }

    }
