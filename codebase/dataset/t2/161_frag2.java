            java.sql.Date[] temp = new java.sql.Date[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getEvalDate();

            }

            sort(temp, 0, temp.length - 1, up);

        }
