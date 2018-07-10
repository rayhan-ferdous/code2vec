        sortingColumns.removeAllElements();

        sortingColumns.addElement(new Integer(column));

        sort(this);

        super.tableChanged(new TableModelEvent(this));

    }



    public void addMouseListenerToHeaderInTable(JTable table) {

        final TableSorter sorter = this;

        final JTable tableView = table;

        tableView.setColumnSelectionAllowed(false);

        MouseAdapter listMouseListener = new MouseAdapter() {



            public void mouseClicked(MouseEvent e) {

                TableColumnModel columnModel = tableView.getColumnModel();

                int viewColumn = columnModel.getColumnIndexAtX(e.getX());

                int column = tableView.convertColumnIndexToModel(viewColumn);

                if (e.getClickCount() == 1 && column != -1) {
