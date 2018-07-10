                Font newFont = new Font(StyleEditionFrame.this.textPreviewLabel.getFont().getFamily(), ((JComboBox) e.getSource()).getSelectedIndex(), StyleEditionFrame.this.textPreviewLabel.getFont().getSize());

                StyleEditionFrame.this.textPreviewLabel.setFont(newFont);

                if (StyleEditionFrame.this.symbolizer != null) {

                    StyleEditionFrame.this.symbolizer.setFont(new fr.ign.cogit.geoxygene.style.Font(newFont));

                }

                StyleEditionFrame.this.layerLegendPanel.getModel().fireActionPerformed(null);
