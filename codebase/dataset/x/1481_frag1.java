        if ("senderReference".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getSenderReference();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("codierzeile".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getCodierzeile();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("recipientID".equals(pColumn)) {
