        if ("filePath".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getFilePath();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("fileHash".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getFileHash();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("fileLength".equals(pColumn)) {
