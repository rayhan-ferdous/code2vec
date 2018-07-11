    private LButton getCmdClose() {

        if (cmdClose == null) {

            cmdClose = new LButton();

            cmdClose.setLocation(new java.awt.Point(332, 126));

            cmdClose.setText("Close");

            cmdClose.setCaptionTag("cmdClose");

            cmdClose.setSize(new java.awt.Dimension(100, 25));

            cmdClose.addActionListener(new java.awt.event.ActionListener() {



                public void actionPerformed(java.awt.event.ActionEvent e) {

                    LanguageFileAdmin.this.onClose();

                }

            });

        }

        return cmdClose;

    }
