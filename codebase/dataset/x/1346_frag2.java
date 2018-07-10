    void nextChannel() {

        int row = ircChannelTable.getSelectedRow();

        if (row < 0) {

            row = -1;

        }

        row++;

        if (row >= ircChannelTable.getRowCount()) {

            row = 0;

        }

        if (row < ircChannelTable.getRowCount()) {

            ircChannelTable.setRowSelectionInterval(row, row);

            ircChannelTable.scrollRectToVisible(ircChannelTable.getCellRect(row, 0, true));

        }

    }
