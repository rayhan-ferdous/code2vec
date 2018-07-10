    private String encodeUri(String uri) {

        String newUri = "";

        StringTokenizer st = new StringTokenizer(uri, "/ ", true);

        while (st.hasMoreTokens()) {

            String tok = st.nextToken();

            if (tok.equals("/")) newUri += "/"; else if (tok.equals(" ")) newUri += "%20"; else {

                try {

                    newUri += URLEncoder.encode(tok, "UTF-8");

                } catch (UnsupportedEncodingException e) {

                    e.printStackTrace();

                }

            }

        }

        return newUri;

    }
