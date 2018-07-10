    public void getExecuteRs(String sql, javax.xml.rpc.holders.IntHolder hRet) throws SQLException {

        Connection conn = null;

        try {

            conn = this.getJdbcTemplate().getDataSource().getConnection();

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.execute();

            pstmt.close();

        } catch (SQLException e) {

            hRet.value = 9;

            throw e;

        } finally {

            if (conn != null) conn.close();

        }

    }
