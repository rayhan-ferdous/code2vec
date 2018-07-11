        if ("marc".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getMarc();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("independent".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getIndependent();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        fSortedColumn = pColumn;
