        if ("instructions4x30".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getInstructions4x30();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("ordererID".equals(pColumn)) {
