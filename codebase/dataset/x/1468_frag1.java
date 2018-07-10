    public static void showErrorDialog(String message, Exception e) {

        String[] s = { "OK", "Details" };

        JOptionPane jo = new JOptionPane();

        if (message != null && !message.equals("")) {

            jo.setMessage(message);

        } else {

            jo.setMessage("Error/Fehler");

        }

        jo.setOptions(s);

        jo.setMessageType(JOptionPane.ERROR_MESSAGE);

        JDialog d = jo.createDialog(null, STANDART_ERROR_MESSAGE);

        d.setVisible(true);

        if (jo.getValue() == s[1]) {

            showStackTraceDialog(e);

        }

    }
