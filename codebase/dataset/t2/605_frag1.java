        if ("billID".equals(pColumn)) {

            Sorter sorter = new Sorter();

            long[] temp = new long[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getBillID();

            }

            fSortOrder = sorter.sortLong(temp, fSortOrder, up);

        }

        if ("type".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getType();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("accountNr".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getAccountNr();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("referenceNr".equals(pColumn)) {
