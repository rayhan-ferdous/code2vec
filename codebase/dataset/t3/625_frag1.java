    public static String getletters(String s) {

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < s.length(); i++) {

            char c = s.charAt(i);

            if (Character.isLetter(c)) {

                result.append(c);

            }

        }

        return s.toString();

    }
