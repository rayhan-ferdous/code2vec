        if ("customerNr".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getCustomerNr();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("sortKey".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getSortKey();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("currencyCode".equals(pColumn)) {
