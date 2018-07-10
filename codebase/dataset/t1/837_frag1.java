            public void run() {

                JApplet applet = data.getApplet();

                Container pane = applet.getContentPane();

                GridBagLayout gridbag = new GridBagLayout();

                GridBagConstraints c = new GridBagConstraints();

                c.fill = GridBagConstraints.BOTH;

                pane.setLayout(gridbag);

                pane.setBackground(java.awt.Color.white);

                pane.setForeground(java.awt.Color.black);

                JPanel reUsePane = new JPanel();

                reUsePane.setBackground(java.awt.Color.white);

                reUsePane.setForeground(java.awt.Color.black);

                reUsePane.setSize(400, 200);

                Util.setFont(data, reUsePane, data.PLAIN);

                GridBagLayout gb = new GridBagLayout();

                GridBagConstraints gbc = new GridBagConstraints();

                reUsePane.setLayout(gb);

                gbc.fill = GridBagConstraints.VERTICAL;

                JLabel alreadyLabel = new JLabel(data.getParamText("savedText"));

                c.gridx = 0;

                c.gridy = 0;

                c.insets = new Insets(0, 0, 0, 0);

                c.weightx = 0.5;

                c.weighty = 0.2;

                c.gridheight = 1;

                c.gridwidth = 5;

                c.ipady = 0;

                c.anchor = GridBagConstraints.WEST;

                c.fill = GridBagConstraints.WEST;

                gridbag.setConstraints(alreadyLabel, c);

                Util.setFont(data, alreadyLabel, data.PLAIN);

                pane.add(alreadyLabel);

                JLabel wantLabel = new JLabel("Do you want to:");

                c.gridx = 0;

                c.gridy = 1;

                c.insets = new Insets(0, 0, 0, 0);

                c.weightx = 0.5;

                c.weighty = 0.2;

                c.gridheight = 1;

                c.gridwidth = 5;

                c.ipady = 0;

                c.anchor = GridBagConstraints.WEST;

                c.fill = GridBagConstraints.WEST;

                gridbag.setConstraints(wantLabel, c);

                Util.setFont(data, wantLabel, data.PLAIN);

                pane.add(wantLabel);

                newCopyB = new JRadioButton(data.getParamText("downloadText"));

                gbc.gridx = 0;

                gbc.gridy = 0;

                gbc.gridwidth = 5;

                gbc.insets = new Insets(0, 0, 0, 0);

                gbc.weightx = 0.1;

                gbc.weighty = 0.1;

                gbc.ipady = 0;

                gbc.anchor = GridBagConstraints.WEST;

                gb.setConstraints(newCopyB, gbc);

                newCopyB.setBackground(java.awt.Color.white);

                newCopyB.setForeground(java.awt.Color.black);

                newCopyB.setSelected(true);

                Util.setFont(data, newCopyB, data.PLAIN);

                reUsePane.add(newCopyB);

                useCopyB = new JRadioButton(data.getParamText("useText"));

                gbc.gridx = 0;

                gbc.gridy = 1;

                gbc.insets = new Insets(0, 0, 0, 0);

                gbc.weightx = 0.1;

                gbc.weighty = 0.1;

                gbc.ipady = 0;

                gbc.anchor = GridBagConstraints.WEST;

                gb.setConstraints(useCopyB, gbc);

                useCopyB.setBackground(java.awt.Color.white);

                useCopyB.setForeground(java.awt.Color.black);

                Util.setFont(data, useCopyB, data.PLAIN);

                reUsePane.add(useCopyB);

                ButtonGroup group = new ButtonGroup();

                group.add(newCopyB);

                group.add(useCopyB);

                c.gridx = 0;

                c.gridy = 2;

                c.insets = new Insets(0, 0, 0, 0);

                c.weightx = 0.5;

                c.weighty = 0.5;

                c.gridheight = 1;

                c.gridwidth = 5;

                c.ipady = 0;

                c.anchor = GridBagConstraints.WEST;

                c.fill = GridBagConstraints.WEST;

                gridbag.setConstraints(reUsePane, c);

                pane.add(reUsePane);

                JPanel buttonPane = new JPanel();

                buttonPane.setBackground(java.awt.Color.white);

                buttonPane.setForeground(java.awt.Color.black);

                buttonPane.setSize(400, 200);

                Util.setFont(data, buttonPane, data.BOLD);

                buttonPane.setLayout(gb);

                gbc.fill = GridBagConstraints.VERTICAL;

                JButton okB = new JButton(data.getParamText("okButtonText"));

                gbc.gridx = 0;

                gbc.gridy = 0;

                gbc.weightx = 0.1;

                gbc.weighty = 0.1;

                gbc.gridheight = 1;

                gbc.gridwidth = 1;

                gbc.ipady = 0;

                gbc.insets = new Insets(30, 30, 30, 30);

                gbc.fill = GridBagConstraints.HORIZONTAL;

                gbc.anchor = GridBagConstraints.WEST;

                gridbag.setConstraints(okB, gbc);

                okB.setForeground(java.awt.Color.black);

                Util.setFont(data, okB, data.BOLD);

                buttonPane.add(okB);

                JButton cancelB = new JButton(data.getParamText("cancelButtonText"));

                gbc.gridx = 1;

                gbc.gridy = 0;

                gbc.weightx = 0.1;

                gbc.weighty = 0.1;

                gbc.gridheight = 1;

                gbc.gridwidth = 1;

                gbc.ipady = 0;

                gridbag.setConstraints(cancelB, gbc);

                cancelB.setForeground(java.awt.Color.black);

                Util.setFont(data, cancelB, data.BOLD);

                buttonPane.add(cancelB);

                JButton helpB = new JButton(data.getParamText("helpButtonText"));

                gbc.gridx = 2;

                gbc.gridy = 0;

                gbc.weightx = 0.1;

                gbc.weighty = 0.1;

                gbc.gridheight = 1;

                gbc.gridwidth = 1;

                gbc.ipady = 0;

                gridbag.setConstraints(helpB, gbc);

                helpB.setForeground(java.awt.Color.black);

                Util.setFont(data, helpB, data.BOLD);

                buttonPane.add(helpB);

                c.gridx = 0;

                c.gridy = 3;

                c.insets = new Insets(30, 30, 30, 30);

                c.weightx = 0.5;

                c.weighty = 0.2;

                c.gridheight = 1;

                c.gridwidth = 3;

                c.ipady = 0;

                c.anchor = GridBagConstraints.WEST;

                c.fill = GridBagConstraints.WEST;

                gridbag.setConstraints(buttonPane, c);

                pane.add(buttonPane);

                applet.validate();

                applet.repaint();

                okB.addActionListener(new java.awt.event.ActionListener() {



                    public void actionPerformed(ActionEvent e) {

                        if (useCopyB.isSelected()) {

                            LaunchDoc launch = new LaunchDoc(data);

                            launch.start();

                            updateScreenInfo();

                        } else downloadFile(data);

                    }

                });

                cancelB.addActionListener(new java.awt.event.ActionListener() {



                    public void actionPerformed(ActionEvent e) {

                        data.setUploadFlag(false);

                        data.setUnlockFlag(true);

                        data.setSavePrevFlag(false);

                        final_actionPerformed(e, data);

                    }

                });

                helpB.addActionListener(new java.awt.event.ActionListener() {



                    public void actionPerformed(ActionEvent e) {

                        try {

                            JSObject win = JSObject.getWindow(data.getApplet());

                            String args[] = { "Help is here" };

                            Object foo = win.call("showHelp", args);

                        } catch (Exception je) {

                        }

                    }

                });

            }
