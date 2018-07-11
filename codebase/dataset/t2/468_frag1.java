        if ("type".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getType();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("message".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getMessage();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        fSortedColumn = pColumn;
