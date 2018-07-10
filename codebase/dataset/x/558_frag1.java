    private void initComponents() {

        toolBarPanel = new javax.swing.JPanel();

        mainToolBar = new javax.swing.JToolBar();

        newButton = new javax.swing.JButton();

        newPortableButton = new javax.swing.JButton();

        openButton = new javax.swing.JButton();

        aboutButton = new javax.swing.JButton();

        exitButton = new javax.swing.JButton();

        jSeparator1 = new javax.swing.JSeparator();

        indexPropertiesPanel = new puggle.ui.IndexPropertiesPanel();

        actionsPanel = new javax.swing.JPanel();

        actionsLabel = new javax.swing.JLabel();

        progressBar = new javax.swing.JProgressBar();

        textArea = new javax.swing.JTextArea();

        startButton = new javax.swing.JButton();

        stopButton = new javax.swing.JButton();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        toolBarPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 1, 1));

        mainToolBar.setFloatable(false);

        mainToolBar.setAlignmentY(0.48387095F);

        newButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/filenew.png")));

        newButton.setToolTipText("Create Index");

        newButton.addActionListener(new java.awt.event.ActionListener() {



            public void actionPerformed(java.awt.event.ActionEvent evt) {

                newButtonActionPerformed(evt);

            }

        });

        mainToolBar.add(newButton);

        newPortableButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/filenew-portable.png")));

        newPortableButton.setToolTipText("Create Portable Index");

        newPortableButton.addActionListener(new java.awt.event.ActionListener() {



            public void actionPerformed(java.awt.event.ActionEvent evt) {

                newPortableButtonActionPerformed(evt);

            }

        });

        mainToolBar.add(newPortableButton);

        openButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fileopen.png")));

        openButton.setToolTipText("Open Index");

        openButton.addActionListener(new java.awt.event.ActionListener() {



            public void actionPerformed(java.awt.event.ActionEvent evt) {

                openButtonActionPerformed(evt);

            }

        });

        mainToolBar.add(openButton);

        aboutButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/help-about.png")));

        aboutButton.setToolTipText("About");

        aboutButton.addActionListener(new java.awt.event.ActionListener() {



            public void actionPerformed(java.awt.event.ActionEvent evt) {

                aboutButtonActionPerformed(evt);

            }

        });

        mainToolBar.add(aboutButton);

        exitButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/exit.png")));

        exitButton.setToolTipText("Exit");

        exitButton.addActionListener(new java.awt.event.ActionListener() {



            public void actionPerformed(java.awt.event.ActionEvent evt) {

                exitButtonActionPerformed(evt);

            }

        });

        mainToolBar.add(exitButton);

        toolBarPanel.add(mainToolBar);

        add(toolBarPanel);

        add(jSeparator1);

        add(indexPropertiesPanel);

        actionsLabel.setBackground(new java.awt.Color(102, 102, 255));

        actionsLabel.setFont(new java.awt.Font("Tahoma", 1, 14));

        actionsLabel.setForeground(new java.awt.Color(255, 255, 255));

        actionsLabel.setText("Actions");

        actionsLabel.setOpaque(true);

        progressBar.setBackground(new java.awt.Color(255, 255, 255));

        progressBar.setForeground(new java.awt.Color(51, 255, 51));

        textArea.setColumns(20);

        textArea.setEditable(false);

        textArea.setFont(new java.awt.Font("Monospaced", 0, 10));

        textArea.setLineWrap(true);

        textArea.setRows(5);

        textArea.setWrapStyleWord(true);

        textArea.setBorder(null);

        textArea.setDisabledTextColor(new java.awt.Color(0, 0, 0));

        textArea.setEnabled(false);

        startButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/start.png")));

        startButton.setText("Start");

        startButton.setToolTipText("Start Indexing");

        startButton.addActionListener(new java.awt.event.ActionListener() {



            public void actionPerformed(java.awt.event.ActionEvent evt) {

                startButtonActionPerformed(evt);

            }

        });

        stopButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stop.png")));

        stopButton.setText("Stop");

        stopButton.setToolTipText("Stop Indexing");

        stopButton.setEnabled(false);

        stopButton.addActionListener(new java.awt.event.ActionListener() {



            public void actionPerformed(java.awt.event.ActionEvent evt) {

                stopButtonActionPerformed(evt);

            }

        });

        org.jdesktop.layout.GroupLayout actionsPanelLayout = new org.jdesktop.layout.GroupLayout(actionsPanel);

        actionsPanel.setLayout(actionsPanelLayout);

        actionsPanelLayout.setHorizontalGroup(actionsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, actionsLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 590, Short.MAX_VALUE).add(actionsPanelLayout.createSequentialGroup().addContainerGap().add(progressBar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 570, Short.MAX_VALUE).addContainerGap()).add(actionsPanelLayout.createSequentialGroup().addContainerGap().add(textArea, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 570, Short.MAX_VALUE).addContainerGap()).add(org.jdesktop.layout.GroupLayout.TRAILING, actionsPanelLayout.createSequentialGroup().addContainerGap(410, Short.MAX_VALUE).add(startButton).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(stopButton).addContainerGap()));

        actionsPanelLayout.setVerticalGroup(actionsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(actionsPanelLayout.createSequentialGroup().add(actionsLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 33, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(progressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(textArea, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(actionsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(stopButton).add(startButton)).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        add(actionsPanel);

    }
