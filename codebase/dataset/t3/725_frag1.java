        if ("dueDate".equals(pColumn)) {

            fComparator = new DateComparator();

            java.sql.Date[] temp = new java.sql.Date[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getDueDate();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("bankAccount".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getBankAccount();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("postAccount".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getPostAccount();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("recipient".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getRecipient();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("beneficiary".equals(pColumn)) {
