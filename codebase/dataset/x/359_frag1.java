            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getPasswd();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("valuntil".equals(pColumn)) {

            fComparator = new TimestampComparator();

            java.sql.Timestamp[] temp = new java.sql.Timestamp[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getValuntil();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("useconfig".equals(pColumn)) {
