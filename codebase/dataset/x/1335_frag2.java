    public static String nonNullString(Object obj, String nullReplacement) {

        String summhelp = null;

        if (obj != null) {

            summhelp = obj.toString();

        }

        if (summhelp == null) {

            summhelp = nullReplacement;

        }

        return summhelp;

    }
