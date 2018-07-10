        if ("tableName".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getTableName();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("privilegeType".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getPrivilegeType();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("isGrantable".equals(pColumn)) {
