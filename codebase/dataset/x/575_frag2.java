        if ("dattablespace".equals(pColumn)) {

            Sorter sorter = new Sorter();

            long[] temp = new long[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getDattablespace();

            }

            fSortOrder = sorter.sortLong(temp, fSortOrder, up);

        }

        if ("datconfig".equals(pColumn)) {

            fComparator = new ObjectComparator();

            Object[] temp = new Object[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getDatconfig();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("datacl".equals(pColumn)) {
