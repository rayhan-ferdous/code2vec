        findJarButton.setText("Browse...");

        findJarButton.addActionListener(new java.awt.event.ActionListener() {



            public void actionPerformed(java.awt.event.ActionEvent evt) {

                findJar(evt);

            }

        });

        gridBagConstraints = new java.awt.GridBagConstraints();

        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;

        driverInputPanel.add(findJarButton, gridBagConstraints);
