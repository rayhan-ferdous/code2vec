        if ("department".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getDepartment();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("company".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getCompany();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("suiteNumber".equals(pColumn)) {
