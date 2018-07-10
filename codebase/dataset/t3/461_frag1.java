    private String execStringQuery(String query) throws THLException {

        ResultSet res = null;

        try {

            res = statement.executeQuery(query);

            String ret = null;

            if (res.next()) {

                ret = res.getString(1);

            }

            return ret;

        } catch (SQLException e) {

            throw new THLException("SQL query failed: " + query, e);

        } finally {

            if (res != null) try {

                res.close();

            } catch (SQLException ignore) {

            }

        }

    }
