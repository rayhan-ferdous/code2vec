    private void createEditPostitsDialog() {

        editPostitsDialog = new FisFrame();

        editPostitsDialog.setAnchor(FisFrame.CENTER);

        postitText = new FisTextArea(10, 40, editPostitsDialog);

        editPostitsDialog.newLine();

        FisPanel pane = new FisPanel();

        editPostitsDialog.addFisPanel(pane, 1, 1);

        epAdd = new FisButton("Add", pane);

        epRemove = new FisButton("Remove", pane);

        FisButton bt3 = new FisButton("Done", pane);

        epAdd.addActionListener(this);

        epRemove.addActionListener(this);

        bt3.addActionListener(this);

        epAdd.setActionCommand("epAdd");

        epRemove.setActionCommand("epRemove");

        bt3.setActionCommand("epDone");

        editPostitsDialog.setTitle(POSTIT_TITLE);

        editPostitsDialog.setStandAlone(false);

        editPostitsDialog.pack();

        mouseAdapter = new DesktopMouseAdapter();

    }
