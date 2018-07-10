    public void testManyParametersStatement() throws Exception {

        final int PARAMS = 2110;

        Statement stmt = con.createStatement();

        makeTestTables(stmt);

        makeObjects(stmt, 10);

        stmt.close();

        StringBuffer sb = new StringBuffer(PARAMS * 3 + 100);

        sb.append("SELECT * FROM #test WHERE f_int in (?");

        for (int i = 1; i < PARAMS; i++) {

            sb.append(", ?");

        }

        sb.append(")");

        try {

            PreparedStatement pstmt = con.prepareStatement(sb.toString());

            for (int i = 1; i <= PARAMS; i++) {

                pstmt.setInt(i, i);

            }

            ResultSet rs = pstmt.executeQuery();

            int cnt = 0;

            while (rs.next()) {

                ++cnt;

            }

            assertEquals(9, cnt);

        } catch (SQLException ex) {

            assertEquals("22025", ex.getSQLState());

        }

    }
