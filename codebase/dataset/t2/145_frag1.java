        if ("journalID".equals(pColumn)) {

            Sorter sorter = new Sorter();

            long[] temp = new long[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getJournalID();

            }

            fSortOrder = sorter.sortLong(temp, fSortOrder, up);

        }

        if ("bookingID".equals(pColumn)) {

            Sorter sorter = new Sorter();

            long[] temp = new long[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getBookingID();

            }

            fSortOrder = sorter.sortLong(temp, fSortOrder, up);

        }

        if ("bookDate".equals(pColumn)) {
