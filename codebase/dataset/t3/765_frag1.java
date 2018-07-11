    public static String base64urlEncode(byte[] byteArray) {

        String base64String = base64Encode(byteArray);

        StringBuffer sb = new StringBuffer(base64String.length());

        for (int i = 0; i < base64String.length(); i++) {

            char ch = base64String.charAt(i);

            if (ch == '+') {

                sb.append('-');

            } else if (ch == '/') {

                sb.append('_');

            } else if (ch == '\r' || ch == '\n') {

            } else if (ch != '=') {

                sb.append(ch);

            }

        }

        return sb.toString();

    }
