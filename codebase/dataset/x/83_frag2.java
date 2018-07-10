    private LMenuItem getFileOpen() {

        if (fileOpen == null) {

            fileOpen = new LMenuItem();

            fileOpen.setText("Open");

            fileOpen.setCaptionTag("fileOpen");

            fileOpen.addActionListener(new java.awt.event.ActionListener() {



                public void actionPerformed(java.awt.event.ActionEvent e) {

                    LanguageFileAdmin.this.onOpen();

                }

            });

        }

        return fileOpen;

    }
