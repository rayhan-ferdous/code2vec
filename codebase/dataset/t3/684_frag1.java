        if ("itemID".equals(pColumn)) {

            Sorter sorter = new Sorter();

            long[] temp = new long[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getItemID();

            }

            fSortOrder = sorter.sortLong(temp, fSortOrder, up);

        }

        if ("locationID".equals(pColumn)) {

            Sorter sorter = new Sorter();

            long[] temp = new long[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getLocationID();

            }

            fSortOrder = sorter.sortLong(temp, fSortOrder, up);

        }

        if ("count".equals(pColumn)) {

            Sorter sorter = new Sorter();

            long[] temp = new long[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getCount();

            }

            fSortOrder = sorter.sortLong(temp, fSortOrder, up);
