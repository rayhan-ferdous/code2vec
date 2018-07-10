    public JLabel getAmountLabel() {

        if (amountLabel == null) {

            amountLabel = new JLabel();

            amountLabel.setText("Amount to transfer");

            amountLabel.setBounds(175, 11, 121, 20);

            setRequiredIcon(amountLabel);

            WidgetProperties.setLabelProperties(amountLabel);

        }

        return amountLabel;

    }
