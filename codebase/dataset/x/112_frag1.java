        if (dirty) {

            switch(JOptionPane.showConfirmDialog(this, "The observation has been modified. Save changes?", "Save changes", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE)) {

                case JOptionPane.YES_OPTION:

                    saveSummary();

                    if (!dirty) dispose();

                    break;

                case JOptionPane.NO_OPTION:

                    dispose();

                    break;
