        if ("routineCatalog".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getRoutineCatalog();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("routineSchema".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getRoutineSchema();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("routineName".equals(pColumn)) {
