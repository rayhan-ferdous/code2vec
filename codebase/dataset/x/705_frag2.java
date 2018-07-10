    private void jButtonFileActionPerformed(java.awt.event.ActionEvent evt) {

        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        fileChooser.setMultiSelectionEnabled(false);

        int answer = fileChooser.showSaveDialog(this);

        if (answer == JFileChooser.APPROVE_OPTION) {

            File file = fileChooser.getSelectedFile();

            if (file != null) {

                jTextFieldFile.setText(file.getAbsolutePath());

            }

        }

    }
