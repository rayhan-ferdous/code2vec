    public Collection getTemplateNames(String virtualWiki) throws Exception {

        logger.debug("Returning template names for " + virtualWiki);

        Collection all = new ArrayList();

        Connection conn = null;

        try {

            conn = DatabaseConnection.getConnection();

            PreparedStatement stmt = conn.prepareStatement(STATEMENT_GET_TEMPLATE_NAMES);

            stmt.setString(1, virtualWiki);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                all.add(rs.getString("name"));

            }

            rs.close();

            stmt.close();

        } finally {

            DatabaseConnection.closeConnection(conn);

        }

        logger.debug(all.size() + " templates exist");

        return all;

    }
