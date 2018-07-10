    public int getColumnIndex(String columnName) {

        String[] columnNames = tableTemplate.getColumnNames();

        for (int i = 0; i < columnNames.length; i++) {

            if (columnNames[i].equals(columnName)) {

                return i;

            }

        }

        return -1;

    }
