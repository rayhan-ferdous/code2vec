            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getDbUser();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("tstamp".equals(pColumn)) {

            fComparator = new TimestampComparator();

            java.sql.Timestamp[] temp = new java.sql.Timestamp[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getTstamp();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("valueDate".equals(pColumn)) {
