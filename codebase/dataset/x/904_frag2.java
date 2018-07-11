    protected java.util.Map<String, String> getParameters(String httpBody) throws IOException {

        java.util.Map<String, String> params = new HashMap<String, String>();

        if (httpBody == null) return params;

        StringTokenizer st = new StringTokenizer(httpBody, "&");

        while (st.hasMoreTokens()) {

            String param = st.nextToken();

            int pos = param.indexOf('=');

            String key = URLDecoder.decode(param.substring(0, pos), "UTF-8");

            String value = URLDecoder.decode(param.substring(pos + 1), "UTF-8");

            if (logger.isDebugEnabled()) {

                logger.debug("HTTP Parameter " + key + "=[" + value + "]");

            }

            params.put(key, value);

        }

        return params;

    }
