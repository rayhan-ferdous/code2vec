    public Recordset(java.sql.Connection conn, String sql, int limit) throws Throwable {

        ResultSet rs = null;

        Statement stmt = null;

        try {

            stmt = conn.createStatement();

            if (limit > 0) stmt.setMaxRows(limit);

            rs = stmt.executeQuery(sql);

            loadRecords(rs);

        } catch (Throwable e) {

            throw e;

        } finally {

            if (rs != null) rs.close();

            if (stmt != null) stmt.close();

        }

    }
