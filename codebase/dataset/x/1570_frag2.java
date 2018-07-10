    public static String convertRequestParams(HttpServletRequest request) {

        Enumeration paramNames = request.getParameterNames();

        StringBuffer buffer = new StringBuffer();

        while (paramNames.hasMoreElements()) {

            String name = (String) paramNames.nextElement();

            String value = request.getParameter(name);

            try {

                buffer.append(URLEncoder.encode(name, UTF8)).append("=").append(URLEncoder.encode(value, UTF8));

            } catch (UnsupportedEncodingException e) {

            }

            if (paramNames.hasMoreElements()) {

                buffer.append("&");

            }

        }

        return buffer.toString();

    }
