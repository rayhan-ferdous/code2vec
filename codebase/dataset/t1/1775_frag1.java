    public String getProperty(String key, String defaultValue, String replace, String to) {

        return replace(getProperty(key, defaultValue), replace, to);

    }



    public List tokenize(String s, List result) {

        StringTokenizer stk = new StringTokenizer(s, ", ");

        while (stk.hasMoreTokens()) {

            String tk = stk.nextToken();

            if (tk.startsWith("$")) {

                tokenize(getProperty(tk.substring(1), ""), result);

            } else {

                result.add(tk);

            }

        }

        return result;

    }



    public String[] tokenize(String s) {

        if (s == null) return null;

        List l = tokenize(s, new LinkedList());

        return (String[]) l.toArray(new String[l.size()]);
