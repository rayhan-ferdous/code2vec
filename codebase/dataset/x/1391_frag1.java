    private void U_fButtonActionPerformed(java.awt.event.ActionEvent evt) {

        FunctionDialog dialog = new FunctionDialog(this, true, language);

        dialog.setVisible(true);

        if (dialog.cancelButtonClicked) return;

        try {

            circuitPanel.addFunction(dialog.function);

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            dialog.removeAll();

            dialog.dispose();

        }

    }
