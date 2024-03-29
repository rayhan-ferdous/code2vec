    public void addMouseListenerToHeaderInTable(JTable table) {

        table_ = table;

        table.getTableHeader().setDefaultRenderer(new TableSorterCellRenderer(table.getTableHeader().getDefaultRenderer()));

        final TableSorter sorter = this;

        final JTable tableView = table;

        tableView.setColumnSelectionAllowed(false);

        MouseAdapter listMouseListener = new MouseAdapter() {



            public void mouseClicked(MouseEvent e) {

                TableColumnModel columnModel = tableView.getColumnModel();

                int viewColumn = columnModel.getColumnIndexAtX(e.getX());

                int column = tableView.convertColumnIndexToModel(viewColumn);

                if (e.getClickCount() == 1 && column != -1) {

                    int shiftPressed = e.getModifiers() & InputEvent.SHIFT_MASK;

                    boolean ascending = (shiftPressed == 0);

                    if ((ascending) && (sorted_column_ == column)) ascending = !ascending_;

                    sorter.sortByColumn(column, ascending);

                }

            }

        };

        JTableHeader th = tableView.getTableHeader();

        th.addMouseListener(listMouseListener);

    }
