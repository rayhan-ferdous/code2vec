        if ("guarantee".equals(pColumn)) {

            Sorter sorter = new Sorter();

            long[] temp = new long[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getGuarantee();

            }

            fSortOrder = sorter.sortLong(temp, fSortOrder, up);

        }

        if ("availability".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getAvailability();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("count".equals(pColumn)) {
