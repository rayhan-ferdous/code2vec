        if ("debitAccountID".equals(pColumn)) {

            Sorter sorter = new Sorter();

            long[] temp = new long[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getDebitAccountID();

            }

            fSortOrder = sorter.sortLong(temp, fSortOrder, up);

        }

        if ("principalID".equals(pColumn)) {

            Sorter sorter = new Sorter();

            long[] temp = new long[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getPrincipalID();

            }

            fSortOrder = sorter.sortLong(temp, fSortOrder, up);

        }

        if ("currencyID".equals(pColumn)) {

            Sorter sorter = new Sorter();

            short[] temp = new short[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getCurrencyID();

            }

            fSortOrder = sorter.sortShort(temp, fSortOrder, up);
