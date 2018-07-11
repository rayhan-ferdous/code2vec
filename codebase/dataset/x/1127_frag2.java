        if ("ahvNumber".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getAhvNumber();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("suvaNumber".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getSuvaNumber();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("nbuNumber".equals(pColumn)) {
