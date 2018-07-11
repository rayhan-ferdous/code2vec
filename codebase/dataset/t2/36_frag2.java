        if ("countryID".equals(pColumn)) {

            Sorter sorter = new Sorter();

            long[] temp = new long[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getCountryID();

            }

            fSortOrder = sorter.sortLong(temp, fSortOrder, up);

        }

        if ("postAccount".equals(pColumn)) {
