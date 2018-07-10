    public String findFirst(String sql, int columnIndex) {

        String result = null;

        Connection conn = null;

        PreparedStatement pstmt = null;

        ResultSet rs = null;

        Transaction tran = null;

        try {

            Session session = SessionFactory.getSession();

            tran = session.beginTransaction();

            conn = session.connection();

            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();

            if (rs.next()) {

                result = rs.getString(columnIndex);

            }

            tran.commit();

        } catch (SQLException e) {

            tran.rollback();

            e.printStackTrace();

        } finally {

            try {

                if (tran != null) {

                    tran = null;

                }

                if (rs != null) {

                    rs.close();

                    rs = null;

                }

                if (pstmt != null) {

                    pstmt.close();

                    pstmt = null;

                }

                if (conn != null) {

                    conn.close();

                    conn = null;

                }

            } catch (SQLException e) {

                e.printStackTrace();

            }

        }

        return result;

    }
