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
