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
