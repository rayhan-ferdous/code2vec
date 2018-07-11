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
