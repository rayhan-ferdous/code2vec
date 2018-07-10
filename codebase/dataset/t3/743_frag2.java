    private JToggleButton getJToggleButton2() {

        if (jToggleButton2 == null) {

            jToggleButton2 = new JToggleButton();

            jToggleButton2.setToolTipText("underline");

            jToggleButton2.setIcon(ICON_TEXT_UNDERLINE);

            jToggleButton2.addActionListener(new java.awt.event.ActionListener() {



                public void actionPerformed(java.awt.event.ActionEvent e) {

                    new StyledEditorKit.UnderlineAction().actionPerformed(e);

                    getJTextPane().requestFocusInWindow();

                }

            });

        }

        return jToggleButton2;

    }
