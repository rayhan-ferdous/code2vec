        if ("priceBuy".equals(pColumn)) {

            fComparator = new BigDecimalComparator();

            java.math.BigDecimal[] temp = new java.math.BigDecimal[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getPriceBuy();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("priceRecommended".equals(pColumn)) {

            fComparator = new BigDecimalComparator();

            java.math.BigDecimal[] temp = new java.math.BigDecimal[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getPriceRecommended();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("priceRecommendedDiscount".equals(pColumn)) {
