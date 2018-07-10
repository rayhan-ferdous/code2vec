    private JTextField getVpidTextField() {

        if (vpidTextField == null) {

            vpidTextField = new RegularExpressionTextField("(\\d*)|(\\d*\\+\\d*)");

            vpidTextField.getDocument().addDocumentListener(new FieldChangeListener());

        }

        return vpidTextField;

    }
