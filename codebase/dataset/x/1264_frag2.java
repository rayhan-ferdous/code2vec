    public static String stripNamedParamsFromQueryString(String queryString, Collection paramNames) {

        String retStr = null;

        if (UtilValidate.isNotEmpty(queryString)) {

            StringTokenizer queryTokens = new StringTokenizer(queryString, "&");

            StringBuffer cleanQuery = new StringBuffer();

            while (queryTokens.hasMoreTokens()) {

                String token = queryTokens.nextToken();

                if (token.startsWith("amp;")) {

                    token = token.substring(4);

                }

                int equalsIndex = token.indexOf("=");

                String name = token;

                if (equalsIndex > 0) {

                    name = token.substring(0, equalsIndex);

                }

                if (!paramNames.contains(name)) {

                    cleanQuery.append(token);

                    if (queryTokens.hasMoreTokens()) {

                        cleanQuery.append("&");

                    }

                }

            }

            retStr = cleanQuery.toString();

        }

        return retStr;

    }
