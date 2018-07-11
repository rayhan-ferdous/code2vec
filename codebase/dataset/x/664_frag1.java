            sort(temp, 0, temp.length - 1, up);

        }

        if ("recipientID".equals(pColumn)) {

            Sorter sorter = new Sorter();

            long[] temp = new long[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getRecipientID();

            }

            fSortOrder = sorter.sortLong(temp, fSortOrder, up);

        }

        if ("beneficiaryID".equals(pColumn)) {

            Sorter sorter = new Sorter();

            long[] temp = new long[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getBeneficiaryID();

            }

            fSortOrder = sorter.sortLong(temp, fSortOrder, up);

        }

        if ("message4x35".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getMessage4x35();

            }

            sort(temp, 0, temp.length - 1, up);

        }
