        if ("firstName".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getFirstName();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("lastName".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getLastName();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("telefon".equals(pColumn)) {
