        private String decodePercent(String str) throws InterruptedException {

            try {

                StringBuffer sb = new StringBuffer();

                for (int i = 0; i < str.length(); i++) {

                    char c = str.charAt(i);

                    switch(c) {

                        case '+':

                            sb.append(' ');

                            break;

                        case '%':

                            sb.append((char) Integer.parseInt(str.substring(i + 1, i + 3), 16));

                            i += 2;

                            break;

                        default:

                            sb.append(c);

                            break;

                    }

                }

                return new String(sb.toString().getBytes());

            } catch (Exception e) {

                sendError(HTTP_BADREQUEST, "BAD REQUEST: Bad percent-encoding.");

                return null;

            }

        }
