            long[] temp = new long[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getOrderType();

            }

            fSortOrder = sorter.sortLong(temp, fSortOrder, up);

        }
