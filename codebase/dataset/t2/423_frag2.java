        if ("name".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getName();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("link".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getLink();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("level".equals(pColumn)) {