        gbc.fill = GridBagConstraints.HORIZONTAL;

        setLayout(gbl);

        if (id == 1) {

            for (int j = 0; j < FIELD_NUM; j++) {

                JLabel label = new JLabel(FIELD_NAMES[j], JLabel.CENTER);

                label.setForeground(Color.black);
