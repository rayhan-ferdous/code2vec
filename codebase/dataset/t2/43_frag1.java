            long[] temp = new long[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getVatID();

            }

            fSortOrder = sorter.sortLong(temp, fSortOrder, up);

        }

        if ("barCode".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getBarCode();

            }

            sort(temp, 0, temp.length - 1, up);

        }
