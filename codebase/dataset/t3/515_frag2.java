    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {

        if (evt.getSource() == removeButton) {

            Vector<String> data = new Vector<String>();

            ListModel model = selectedGenes.getModel();

            int[] indices = selectedGenes.getSelectedIndices();

            int j;

            for (int i = 0; i < model.getSize(); i++) {

                for (j = 0; j < indices.length; j++) {

                    if (indices[j] == i) {

                        break;

                    }

                }

                if (j == indices.length) {

                    data.add((String) model.getElementAt(i));

                }

            }

            selectedGenes.setListData(data);

            allGenes.clearSelection();

        }

    }
