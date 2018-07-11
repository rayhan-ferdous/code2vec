    @Override

    protected void customizeCommands(Commander commander) {

        setViewlAction = new AbstractAction("show-view-panel") {



            public void actionPerformed(ActionEvent event) {

                setActivePanel(VIEW_PANEL);

            }

        };

        commander.registerAction(setViewlAction);

        setPVsAction = new AbstractAction("show-set-pvs-panel") {



            public void actionPerformed(ActionEvent event) {

                setActivePanel(SET_PVS_PANEL);

            }

        };

        commander.registerAction(setPVsAction);

        setAcceleratorAction = new AbstractAction("show-set-accelerator-panel") {



            public void actionPerformed(ActionEvent event) {

                JFileChooser ch = new JFileChooser();

                ch.setDialogTitle("READ ACCELERATOR DATA XML FILE");

                if (acceleratorDataFile != null) {

                    ch.setSelectedFile(acceleratorDataFile);

                }

                int returnVal = ch.showOpenDialog(setPVsPanel);

                if (returnVal == JFileChooser.APPROVE_OPTION) {

                    acceleratorDataFile = ch.getSelectedFile();

                    String path = acceleratorDataFile.getAbsolutePath();

                    pvsSelector.setAcceleratorFileName(path);

                }

            }

        };

        commander.registerAction(setAcceleratorAction);

        setAcceleratorAction.setEnabled(false);

        setPredefConfigAction = new AbstractAction("set-predef-config") {



            public void actionPerformed(ActionEvent event) {

                setActivePanel(PREDEF_CONF_PANEL);

            }

        };

        commander.registerAction(setPredefConfigAction);

    }
