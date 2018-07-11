                }

                return list;

            } finally {

                if (rs != null) rs.close();

                if (stmt != null) stmt.close();

                if (conn != null) conn.close();

            }

        } catch (SQLException e) {

            throw new FidoDatabaseException(e);

        }

    }
