            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getSex();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("nationalityID".equals(pColumn)) {
