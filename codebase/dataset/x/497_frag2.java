            for (int i = 0; i < result.length; i++) {

                ps.setLong(1, result[i].getId());

                rs = ps.executeQuery();

                while (rs.next()) {

                    String lang = rs.getString(1);

                    result[i].setDesc(lang, rs.getString(2));
