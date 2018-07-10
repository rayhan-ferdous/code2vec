        if ("nbuNumber".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getNbuNumber();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("modified".equals(pColumn)) {

            fComparator = new TimestampComparator();

            java.sql.Timestamp[] temp = new java.sql.Timestamp[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getModified();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("validFrom".equals(pColumn)) {
