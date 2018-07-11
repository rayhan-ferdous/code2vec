    protected String replace(String str, String pattern, String replaceWith) {

        int s = 0;

        int e = 0;

        StringBuffer result = new StringBuffer();

        while ((e = str.indexOf(pattern, s)) >= 0) {

            result.append(str.substring(s, e));

            if (replaceWith != null) result.append(replaceWith);

            s = e + pattern.length();

        }

        result.append(str.substring(s));

        return result.toString();

    }
