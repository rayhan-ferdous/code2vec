    private JCheckBox getCreateJar() {

        if (createJar == null) {

            createJar = new JCheckBox("JAR");

            createJar.setPreferredSize(new Dimension(50, 20));

            createJar.setBounds(new Rectangle(310, 62, 50, 20));

        }

        return createJar;

    }
