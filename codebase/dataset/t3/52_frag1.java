    private JMenuItem getSaveEPSMenuItem() {

        if (saveEPSMenuItem == null) {

            saveEPSMenuItem = new JMenuItem();

            saveEPSMenuItem.setText("Save current view as EPS image");

            saveEPSMenuItem.setEnabled(false);

            saveEPSMenuItem.addActionListener(this);

        }

        return saveEPSMenuItem;

    }
