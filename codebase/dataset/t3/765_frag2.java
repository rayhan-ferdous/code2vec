    public static byte[] base64urlDecode(String s) throws IOException {

        int length = s.length();

        StringBuffer sb = new StringBuffer(length);

        int i = 0;

        while (i < length || (i & 2) != 0) {

            if (i >= length) {

                sb.append('=');

            } else {

                char ch = s.charAt(i);

                if (ch == '-') {

                    sb.append('+');

                } else if (ch == '_') {

                    sb.append('/');

                } else {

                    sb.append(ch);

                }

            }

            i++;

        }

        return base64Decode(sb.toString());

    }
