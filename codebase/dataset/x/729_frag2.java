                ps = con.prepareStatement(sql);

                ps.executeUpdate(sql);

            } else {

                ps = con.prepareStatement(sql);

                rs = ps.executeQuery(sql);

                ResultSetMetaData rsmd = rs.getMetaData();

                int[] width = new int[rsmd.getColumnCount() + 1];
