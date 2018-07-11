        if ("datname".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getDatname();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("datdba".equals(pColumn)) {

            Sorter sorter = new Sorter();

            long[] temp = new long[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getDatdba();

            }

            fSortOrder = sorter.sortLong(temp, fSortOrder, up);

        }

        if ("encoding".equals(pColumn)) {

            Sorter sorter = new Sorter();

            long[] temp = new long[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getEncoding();

            }

            fSortOrder = sorter.sortLong(temp, fSortOrder, up);

        }

        if ("datistemplate".equals(pColumn)) {
