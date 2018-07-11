        if (!"u".equals(pSortDirection)) {

            up = false;

        }

        if ("id".equals(pColumn)) {

            Sorter sorter = new Sorter();

            long[] temp = new long[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getId();

            }

            fSortOrder = sorter.sortLong(temp, fSortOrder, up);

        }

        if ("orderID".equals(pColumn)) {

            Sorter sorter = new Sorter();

            long[] temp = new long[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getOrderID();

            }

            fSortOrder = sorter.sortLong(temp, fSortOrder, up);

        }

        if ("itemID".equals(pColumn)) {

            Sorter sorter = new Sorter();

            long[] temp = new long[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getItemID();

            }

            fSortOrder = sorter.sortLong(temp, fSortOrder, up);

        }

        if ("itemDetailID".equals(pColumn)) {
