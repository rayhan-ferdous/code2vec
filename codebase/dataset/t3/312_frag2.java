        private void displayDownloadingMessage() {

            SwingUtilities.invokeLater(new Runnable() {



                public void run() {

                    messageLabel.setText("Downloading data.");

                    outputArea.setCursor(new Cursor(Cursor.WAIT_CURSOR));

                    outputArea.setText("");

                }

            });

        }
