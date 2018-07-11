            Object[] temp = new Object[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getNspacl();

            }

            sort(temp, 0, temp.length - 1, up);

        }
