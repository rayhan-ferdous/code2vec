        } finally {

            if (rs != null) {

                try {

                    rs.close();

                } catch (SQLException sqlEx) {

                    rs = null;

                }

                if (stmt != null) {

                    try {

                        stmt.close();

                    } catch (SQLException sqlEx) {

                        stmt = null;
