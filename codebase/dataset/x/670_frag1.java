        if ("customerID".equals(pColumn)) {

            Sorter sorter = new Sorter();

            long[] temp = new long[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getCustomerID();

            }

            fSortOrder = sorter.sortLong(temp, fSortOrder, up);

        }

        if ("amount".equals(pColumn)) {

            fComparator = new BigDecimalComparator();

            java.math.BigDecimal[] temp = new java.math.BigDecimal[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getAmount();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("amountPaid".equals(pColumn)) {
