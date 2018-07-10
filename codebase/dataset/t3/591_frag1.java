    private void _cleanUp(Connection con, Statement s, ResultSet rs) {

        if (rs != null) {

            try {

                rs.close();

            } catch (Exception e) {

            }

        }

        if (s != null) {

            try {

                s.close();

            } catch (Exception e) {

            }

        }

        if (con != null) {

            try {

                con.close();

            } catch (Exception e) {

            }

        }

    }
