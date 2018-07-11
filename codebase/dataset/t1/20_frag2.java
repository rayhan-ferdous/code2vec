    public Vector getMultiSelectedRowKeys() throws KExceptionClass {

        int[] indexes = visualTable.getSelectedRows();

        if (indexes.length == 0) {

            throw new KExceptionClass("\nYou need to chose records!", null);

        }

        Vector selectedKeys = new Vector();

        for (int i = 0; i < indexes.length; i++) {

            selectedKeys.add(new Long(tableModel.getKeyValue(indexes[i])));

        }

        return selectedKeys;

    }
