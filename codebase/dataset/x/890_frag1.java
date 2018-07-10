            public void actionPerformed(ActionEvent e) {

                JFileChooser chooser = new JFileChooser(".");

                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                File file = new File(".");

                if (chooser.showDialog(myPanel, "Select") == JFileChooser.APPROVE_OPTION) {

                    file = chooser.getSelectedFile();

                    outdirfile.setText(file.getPath());

                    sourceDir = file;

                }

            }
