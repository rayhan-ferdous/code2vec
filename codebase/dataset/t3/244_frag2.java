            double[] temp = new double[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getDiscountPercent();

            }

            fSortOrder = sorter.sortDouble(temp, fSortOrder, up);

        }

        if ("discountAmount".equals(pColumn)) {

            fComparator = new BigDecimalComparator();

            java.math.BigDecimal[] temp = new java.math.BigDecimal[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getDiscountAmount();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("modified".equals(pColumn)) {
