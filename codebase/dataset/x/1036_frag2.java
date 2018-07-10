    public void setRow(int row, Vector<String> data) {

        SpreadsheetModel DataSpreadsheetModel = (SpreadsheetModel) getModel();

        edit = new StateEdit(this, "set row " + row);

        if (row >= getRowCount()) {

            for (int r = getRowCount(); r <= row; ++r) {

                DataSpreadsheetModel.insertRow(r);

                updateRowHeader();

            }

        }

        if (data.size() >= getColumnCount()) {

            for (int c = getColumnCount(); c <= data.size(); ++c) {

                DataSpreadsheetModel.insertColumn(c);

            }

        }

        DataSpreadsheetModel.setRow(row, data);

        setChangedStatus();

        edit.end();

        undoableEditSupport.postEdit(edit);

    }
