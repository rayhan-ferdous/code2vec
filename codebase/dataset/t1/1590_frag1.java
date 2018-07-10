    public String[][] Rptlist(String q1) {

        try {

            CommonService x1 = new CommonService();

            Connection c1 = x1.initiateCon();

            ResultSet rs1;

            rs1 = x1.getResultSet(c1, q1);

            ResultSetMetaData metadata1 = rs1.getMetaData();

            int numcols = metadata1.getColumnCount();

            rs1.last();

            int numrows = rs1.getRow();

            rs1.first();

            String[][] data = new String[numrows + 1][numcols];

            int r = 0;

            int c = 0;

            int o = 0;

            String str;

            for (int x = 1; x <= numcols; x++) {

                data[r][x - 1] = metadata1.getColumnLabel(x);

            }

            r++;

            while (true) {

                for (c = 0; c < numcols; c++) {

                    o = c + 1;

                    str = rs1.getString(o);

                    data[r][c] = str;

                }

                r++;

                if (!rs1.next()) break;

            }

            x1.closeCon(c1);

            return data;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }
