        if ("bvgAmountEmployee".equals(pColumn)) {

            fComparator = new BigDecimalComparator();

            java.math.BigDecimal[] temp = new java.math.BigDecimal[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getBvgAmountEmployee();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("bvgAmountEmployer".equals(pColumn)) {

            fComparator = new BigDecimalComparator();

            java.math.BigDecimal[] temp = new java.math.BigDecimal[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getBvgAmountEmployer();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("bvgRiskPremium".equals(pColumn)) {
