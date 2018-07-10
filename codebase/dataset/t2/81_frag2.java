        if ("nspowner".equals(pColumn)) {

            Sorter sorter = new Sorter();

            long[] temp = new long[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getNspowner();

            }

            fSortOrder = sorter.sortLong(temp, fSortOrder, up);

        }

        if ("nspacl".equals(pColumn)) {

            fComparator = new ObjectComparator();

            Object[] temp = new Object[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getNspacl();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        fSortedColumn = pColumn;
