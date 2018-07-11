    void clearClientLogs() {

        String qry = "DELETE FROM log WHERE mediaid = '-1'";

        Statement stmt = null;

        try {

            stmt = conn.createStatement();

            stmt.executeUpdate(qry);

        } catch (SQLException e) {

            LOG.error("SQL error", e);

            throw new RuntimeException(e);

        } finally {

            try {

                if (stmt != null) stmt.close();

            } catch (SQLException e) {

                LOG.error("SQL error", e);

                throw new RuntimeException(e);

            }

        }

    }
