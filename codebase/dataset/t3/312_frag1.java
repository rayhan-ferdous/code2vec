        void addText(final String line) {

            EventQueue.invokeLater(new Runnable() {



                public void run() {

                    textArea.append(line);

                    textArea.append("\n");

                    scrollBar.setValue(scrollBar.getMaximum());

                }

            });

        }
