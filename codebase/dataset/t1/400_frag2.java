    public TransferPopupMenu(JTable source) {

        super();

        this.source = source;

        openFile = makeMenuItem("Open File");

        this.add(openFile);

        openDir = makeMenuItem("Open Directory");

        this.add(openDir);

        this.add(new JSeparator());

        remove = makeMenuItem("Remove transfer");

        this.add(remove);

        clear = makeMenuItem("Remove all inactive transfers");

        this.add(clear);

    }
