                public void actionPerformed(ActionEvent ae) {

                    try {

                        dataPanelManager.createDataPanel(extension);

                    } catch (Exception e) {

                        log.error("Unable to open data panel provided by extension " + extension.getName() + " (" + extension.getID() + ").");

                        e.printStackTrace();

                    }

                }
