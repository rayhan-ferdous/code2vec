                    public void run() {

                        try {

                            runFile(IMAGE_PROGRAM, myFileExplorer.getSelectedFile());

                        } catch (Exception e) {

                            JOptionPane.showMessageDialog(null, "Error run program file " + e, "Warning", JOptionPane.WARNING_MESSAGE);

                        }

                        ;

                    }
