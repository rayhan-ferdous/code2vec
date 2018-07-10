        if ("qstCode".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getQstCode();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("qstSex".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getQstSex();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("qstChildren".equals(pColumn)) {

            Sorter sorter = new Sorter();

            long[] temp = new long[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getQstChildren();

            }

            fSortOrder = sorter.sortLong(temp, fSortOrder, up);

        }

        if ("qstAmountStart".equals(pColumn)) {

            fComparator = new BigDecimalComparator();

            java.math.BigDecimal[] temp = new java.math.BigDecimal[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getQstAmountStart();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("qstAmountEnd".equals(pColumn)) {

            fComparator = new BigDecimalComparator();

            java.math.BigDecimal[] temp = new java.math.BigDecimal[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getQstAmountEnd();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("qstPercentage".equals(pColumn)) {

            fComparator = new BigDecimalComparator();

            java.math.BigDecimal[] temp = new java.math.BigDecimal[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getQstPercentage();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("qstAmount".equals(pColumn)) {
