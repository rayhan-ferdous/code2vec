        if ("currencyBookID".equals(pColumn)) {

            Sorter sorter = new Sorter();

            short[] temp = new short[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getCurrencyBookID();

            }

            fSortOrder = sorter.sortShort(temp, fSortOrder, up);

        }

        if ("amount".equals(pColumn)) {

            fComparator = new BigDecimalComparator();

            java.math.BigDecimal[] temp = new java.math.BigDecimal[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getAmount();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("currencyPayID".equals(pColumn)) {
