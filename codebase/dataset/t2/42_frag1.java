        if ("orderDate".equals(pColumn)) {

            fComparator = new DateComparator();

            java.sql.Date[] temp = new java.sql.Date[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getOrderDate();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("shippingDate".equals(pColumn)) {

            fComparator = new DateComparator();

            java.sql.Date[] temp = new java.sql.Date[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getShippingDate();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("priceTotal".equals(pColumn)) {
