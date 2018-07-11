    public static void setFont(RSyntaxTextArea textArea, Font font) {

        if (font != null) {

            SyntaxScheme ss = textArea.getSyntaxScheme();

            ss = (SyntaxScheme) ss.clone();

            for (int i = 0; i < ss.styles.length; i++) {

                if (ss.styles[i] != null) {

                    ss.styles[i].font = font;

                }

            }

            textArea.setSyntaxScheme(ss);

            textArea.setFont(font);

        }

    }
