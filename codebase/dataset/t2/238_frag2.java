        if ("datlastsysoid".equals(pColumn)) {

            Sorter sorter = new Sorter();

            long[] temp = new long[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getDatlastsysoid();

            }

            fSortOrder = sorter.sortLong(temp, fSortOrder, up);

        }

        if ("datvacuumxid".equals(pColumn)) {
