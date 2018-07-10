    private void invQftButtonActionPerformed(java.awt.event.ActionEvent evt) {

        GateDialog dialog = new GateDialog(this, true, language, "invQFT", xRegisterSize, yRegisterSize);

        dialog.setVisible(true);

        if (dialog.cancelButtonClicked) return;

        try {

            circuitPanel.addInvQFT(dialog.yRegisterChosen);

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            dialog.removeAll();

            dialog.dispose();

        }

    }
