    public static String join(String sep, String... words) {

        StringBuffer s = new StringBuffer();

        for (int i = 0; i < words.length; i++) {

            s.append(words[i]);

            if (i < words.length - 1) {

                s.append(sep);

            }

        }

        return s.toString();

    }
