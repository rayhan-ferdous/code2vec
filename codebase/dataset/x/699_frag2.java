        if ("rejectCode".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getRejectCode();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("currencyCode2".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getCurrencyCode2();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("cost".equals(pColumn)) {
