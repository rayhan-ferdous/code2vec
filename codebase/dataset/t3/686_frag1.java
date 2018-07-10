        if ("userID".equals(pColumn)) {

            Sorter sorter = new Sorter();

            long[] temp = new long[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getUserID();

            }

            fSortOrder = sorter.sortLong(temp, fSortOrder, up);

        }

        if ("modified".equals(pColumn)) {

            fComparator = new TimestampComparator();

            java.sql.Timestamp[] temp = new java.sql.Timestamp[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getModified();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        fSortedColumn = pColumn;
