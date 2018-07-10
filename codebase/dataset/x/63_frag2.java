    public static String revertXMLSafe(String text) {

        try {

            text = text.replaceAll("&aacute;", "á");

            text = text.replaceAll("&eacute;", "é");

            text = text.replaceAll("&iacute;", "í");

            text = text.replaceAll("&oacute;", "ó");

            text = text.replaceAll("&uacute;", "ú");

            text = text.replaceAll("&Aacute;", "Á");

            text = text.replaceAll("&Eacute;", "É");

            text = text.replaceAll("&Iacute;", "Í");

            text = text.replaceAll("&Oacute;", "Ó");

            text = text.replaceAll("&Uacute;", "Ú");

            text = text.replaceAll("&ntilde;", "ñ");

            text = text.replaceAll("&Ntilde;", "Ñ");

            text = text.replaceAll("&amp;", "&");

        } catch (Exception e) {

        }

        return text;

    }
