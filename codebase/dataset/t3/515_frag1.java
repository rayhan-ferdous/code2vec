    private void removeCladeButtonActionPerformed(java.awt.event.ActionEvent evt) {

        if (evt.getSource() == removeCladeButton) {

            int[] indices = cladeList.getSelectedIndices();

            if (indices.length == 0) {

                JOptionPane.showMessageDialog(null, "Please select a clade first");

                return;

            }

            Vector data = new Vector();

            ListModel model = cladeList.getModel();

            int j;

            for (int i = 0; i < model.getSize(); i++) {

                for (j = 0; j < indices.length; j++) if (indices[j] == i) break;

                if (j == indices.length) data.add(model.getElementAt(i));

            }

            cladeList.setListData(data);

        }

    }
