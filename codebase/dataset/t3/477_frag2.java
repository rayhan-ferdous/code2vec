        if ("currencyID".equals(pColumn)) {

            Sorter sorter = new Sorter();

            short[] temp = new short[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getCurrencyID();

            }

            fSortOrder = sorter.sortShort(temp, fSortOrder, up);

        }

        if ("creationDate".equals(pColumn)) {

            fComparator = new DateComparator();

            java.sql.Date[] temp = new java.sql.Date[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getCreationDate();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("valueDate".equals(pColumn)) {

            fComparator = new DateComparator();

            java.sql.Date[] temp = new java.sql.Date[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getValueDate();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("bookText".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getBookText();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("referenceNumber".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getReferenceNumber();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("modified".equals(pColumn)) {
