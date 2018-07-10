        if ("referenceNumber".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getReferenceNumber();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("debitAccountID".equals(pColumn)) {

            Sorter sorter = new Sorter();

            long[] temp = new long[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getDebitAccountID();

            }

            fSortOrder = sorter.sortLong(temp, fSortOrder, up);

        }

        if ("principalID".equals(pColumn)) {
