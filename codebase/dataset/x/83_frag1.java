    private JMenuItem getResumeDownloadsMenuItem() {

        debug.print("");

        if (resumeDownloadsMenuItem == null) {

            resumeDownloadsMenuItem = new JCheckBoxMenuItem();

            resumeDownloadsMenuItem.setText("Resume Downloads");

            resumeDownloadsMenuItem.addItemListener(new ItemListener() {



                public void itemStateChanged(ItemEvent e) {

                    AbstractButton button = (AbstractButton) e.getItem();

                    config.resumeDownloads = button.isSelected();

                }

            });

        }

        return resumeDownloadsMenuItem;

    }
