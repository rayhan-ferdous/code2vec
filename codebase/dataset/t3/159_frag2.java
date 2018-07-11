        if ("grantee".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getGrantee();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("specificCatalog".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getSpecificCatalog();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("specificSchema".equals(pColumn)) {
