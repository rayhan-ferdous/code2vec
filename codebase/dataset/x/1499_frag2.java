    private JMenu getJMenuImport() {

        if (jMenuImport == null) {

            jMenuImport = new JMenu("Import");

            jMenuImport.setMnemonic('I');

        }

        jMenuImport.add(this.getJMenuItemImport());

        jMenuImport.addSeparator();

        jMenuImport.add(this.getJMenuItemDownload());

        return jMenuImport;

    }
