            boolean[] temp = new boolean[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getQst();

            }

            fSortOrder = sorter.sortBoolean(temp, fSortOrder, up);

        }
