            java.math.BigDecimal[] temp = new java.math.BigDecimal[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getPrice();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("vatID".equals(pColumn)) {
