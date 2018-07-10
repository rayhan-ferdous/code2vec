    public static String urlDecode(String input) {

        if (input == null) {

            return null;

        }

        try {

            return URLDecoder.decode(input, UTF8);

        } catch (UnsupportedEncodingException e) {

            return null;

        }

    }
