    private JButton getJButtonFilename() {

        if (jButtonFilename == null) {

            jButtonFilename = new JButton();

            jButtonFilename.setBounds(new Rectangle(514, 361, 130, 26));

            jButtonFilename.setText("Display Image");

            jButtonFilename.addActionListener(new java.awt.event.ActionListener() {



                public void actionPerformed(java.awt.event.ActionEvent e) {

                    getJPanelImage().setImage(App.RunGetFile(getJTextFieldFilename().getText(), DirName, Type, FileName));

                    repaint();

                }

            });

        }

        return jButtonFilename;

    }
