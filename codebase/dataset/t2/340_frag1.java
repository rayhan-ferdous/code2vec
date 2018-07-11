        if ("postAccount".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getPostAccount();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("bankAccount".equals(pColumn)) {
