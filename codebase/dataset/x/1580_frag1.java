    public static boolean isEmpty(CharSequence s) {

        if (s != null) {

            for (int i = 0; i < s.length(); i++) {

                if (!Character.isWhitespace(s.charAt(i))) {

                    return false;

                }

            }

        }

        return true;

    }
