        if ("sizeUnit".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getSizeUnit();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("datasheetLink".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getDatasheetLink();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("manufacturerID".equals(pColumn)) {
