    private LMenuItem getLocaleDetails() {

        if (localeDetails == null) {

            localeDetails = new LMenuItem();

            localeDetails.setCaptionTag("localeDetails");

            localeDetails.addActionListener(new java.awt.event.ActionListener() {



                public void actionPerformed(java.awt.event.ActionEvent e) {

                    onLocaleDetails();

                }

            });

        }

        return localeDetails;

    }
