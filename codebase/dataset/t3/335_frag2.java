        if ("datasheetLink".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getDatasheetLink();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("manufacturerID".equals(pColumn)) {

            Sorter sorter = new Sorter();

            long[] temp = new long[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getManufacturerID();

            }

            fSortOrder = sorter.sortLong(temp, fSortOrder, up);

        }

        if ("manufacturer".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getManufacturer();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("manufacturerModelNr".equals(pColumn)) {
