    int compares;



    public TableSorter() {

        indexes = new int[0];

    }



    public TableSorter(TableModel model) {

        setModel(model);

    }



    public void setModel(TableModel model) {

        super.setModel(model);

        reallocateIndexes();

    }



    public int compareRowsByColumn(int row1, int row2, int column) {

        Class type = model.getColumnClass(column);

        TableModel data = model;

        Object o1 = data.getValueAt(row1, column);

        Object o2 = data.getValueAt(row2, column);

        if (o1 == null && o2 == null) {

            return 0;

        } else if (o1 == null) {

            return -1;

        } else if (o2 == null) {

            return 1;
