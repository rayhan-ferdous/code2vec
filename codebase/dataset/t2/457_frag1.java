        if ("bcType".equals(pColumn)) {

            Sorter sorter = new Sorter();

            long[] temp = new long[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getBcType();

            }

            fSortOrder = sorter.sortLong(temp, fSortOrder, up);

        }

        if ("sic".equals(pColumn)) {

            Sorter sorter = new Sorter();

            long[] temp = new long[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getSic();

            }

            fSortOrder = sorter.sortLong(temp, fSortOrder, up);

        }

        if ("eurosic".equals(pColumn)) {
