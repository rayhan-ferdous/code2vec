        if ("id".equals(pColumn)) {

            Sorter sorter = new Sorter();

            long[] temp = new long[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getId();

            }

            fSortOrder = sorter.sortLong(temp, fSortOrder, up);

        }

        if ("codeAn".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getCodeAn();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("nameShort".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getNameShort();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("name".equals(pColumn)) {
