        if ("type2".equals(pColumn)) {

            Sorter sorter = new Sorter();

            long[] temp = new long[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getType2();

            }

            fSortOrder = sorter.sortLong(temp, fSortOrder, up);

        }

        if ("ort18".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getOrt18();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("ort".equals(pColumn)) {
