            short[] temp = new short[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getCurrencyDebitID();

            }

            fSortOrder = sorter.sortShort(temp, fSortOrder, up);

        }
