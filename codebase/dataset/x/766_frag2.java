                            GuiUtils.showErrorMessage(mainFrame, "Failed to connect to server - " + e, "Connection faliure");

                            admin.disconnect();

                            showDisconnected(false);

                            e.printStackTrace();

                        }

                    } else JOptionPane.showMessageDialog(mainFrame, "Invalid servername", "Error", JOptionPane.ERROR_MESSAGE);

                }

            } finally {

                connectDialog = null;

            }

        }

    }



    /**

	 * Disconnects from the server.

	 */

    synchronized void doDisconnect() {

        if (this.currentState != STATE_DISCONNECTED) {
