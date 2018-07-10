    private void toffoliButtonActionPerformed(java.awt.event.ActionEvent evt) {

        GateDialog dialog = new GateDialog(this, true, language, "Toffoli", xRegisterSize, yRegisterSize);

        dialog.setVisible(true);

        if (dialog.cancelButtonClicked) return;

        try {

            int[] qubits = dialog.getQubits();

            circuitPanel.addToffoli(qubits, dialog.yRegisterChosen);

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            dialog.removeAll();

            dialog.dispose();

        }

    }
