    public static String breakpointParseClass(String name) {

        int dot = name.lastIndexOf('.');

        int colon = name.indexOf(':');

        if (dot != -1) {

            return name.substring(0, dot);

        } else if (dot == -1 && colon != -1) {

            return name.substring(0, colon);

        } else {

            return "";

        }

    }
