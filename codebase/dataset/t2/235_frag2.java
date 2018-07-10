        if ("ledgerID".equals(pColumn)) {

            Sorter sorter = new Sorter();

            long[] temp = new long[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getLedgerID();

            }

            fSortOrder = sorter.sortLong(temp, fSortOrder, up);

        }

        if ("accountID".equals(pColumn)) {
