        if ("key".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getKey();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("value".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getValue();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("modified".equals(pColumn)) {
