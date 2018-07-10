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

        if ("message4x28".equals(pColumn)) {
