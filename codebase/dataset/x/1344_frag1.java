    protected static byte[] sendPostFormUrlencodedReadBody(final String baseURL, final Map<String, String> fields) throws MalformedURLException, IOException {

        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        HttpURLConnection conn = null;

        InputStream in = null;

        try {

            conn = sendPostFormUrlencoded(baseURL, fields);

            LOG.info("Response (" + conn.getResponseCode() + "): " + conn.getResponseMessage());

            conn.getResponseCode();

            in = conn.getInputStream();

            int b;

            while ((b = in.read()) != -1) {

                bout.write(b);

            }

            return bout.toByteArray();

        } finally {

            if (in != null) {

                try {

                    in.close();

                } catch (IOException ignored) {

                }

            }

            conn.disconnect();

        }

    }
