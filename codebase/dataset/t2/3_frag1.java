            short[] temp = new short[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getId();

            }

            fSortOrder = sorter.sortShort(temp, fSortOrder, up);

        }

        if ("isoCode".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getIsoCode();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("isoCodeNum".equals(pColumn)) {
