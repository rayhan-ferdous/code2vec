        public void actionPerformed(ActionEvent evt) {

            try {

                doFitInWindow();

            } catch (NoninvertibleTransformException nte) {

                nte.printStackTrace();

            }

        }
