    public static boolean isNum(String s) {

        try {

            Double.parseDouble(s);

        } catch (NumberFormatException nfe) {

            return false;

        }

        return true;

    }
