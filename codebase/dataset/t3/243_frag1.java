            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getOrt18();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("ort".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getOrt();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("kanton".equals(pColumn)) {
