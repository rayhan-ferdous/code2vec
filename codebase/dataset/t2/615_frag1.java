        if ("grantor".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getGrantor();

            }

            sort(temp, 0, temp.length - 1, up);

        }

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

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getSpecificSchema();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("specificName".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getSpecificName();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("routineCatalog".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getRoutineCatalog();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("routineSchema".equals(pColumn)) {
