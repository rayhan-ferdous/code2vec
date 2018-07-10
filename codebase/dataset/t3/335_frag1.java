        if ("price".equals(pColumn)) {

            fComparator = new BigDecimalComparator();

            java.math.BigDecimal[] temp = new java.math.BigDecimal[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getPrice();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("currencyID".equals(pColumn)) {

            Sorter sorter = new Sorter();

            short[] temp = new short[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getCurrencyID();

            }

            fSortOrder = sorter.sortShort(temp, fSortOrder, up);

        }

        if ("discount".equals(pColumn)) {

            fComparator = new BigDecimalComparator();

            java.math.BigDecimal[] temp = new java.math.BigDecimal[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getDiscount();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("discountPercent".equals(pColumn)) {
