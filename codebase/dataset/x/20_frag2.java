    public static boolean isValidTime(String strTime) {

        try {

            return strToTime(strTime) != null;

        } catch (Exception e) {

            return false;

        }

    }
