        if ("type".equals(pColumn)) {

            Sorter sorter = new Sorter();

            long[] temp = new long[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getType();

            }

            fSortOrder = sorter.sortLong(temp, fSortOrder, up);

        }

        if ("locale".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getLocale();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("key".equals(pColumn)) {
