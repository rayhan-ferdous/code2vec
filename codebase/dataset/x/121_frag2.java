    private void edittypesActionPerformed(java.awt.event.ActionEvent evt) {

        try {

            TaskConfigurator.getReference().setVisible(true);

        } catch (Exception e) {

            Errmsg.errmsg(e);

        }

    }
