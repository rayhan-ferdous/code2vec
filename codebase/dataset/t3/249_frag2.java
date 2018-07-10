    void processLetRegion(Vector region) throws IOException {

        if (DEBUG) System.out.println("params=\"" + params + "\"");

        StringReader sr = new StringReader(params);

        StreamTokenizer pst = new StreamTokenizer(sr);

        pst.ordinaryChar('-');

        pst.ordinaryChar('/');

        pst.ordinaryChar('*');

        pst.wordChars('@', '@');

        pst.wordChars('_', '_');

        int tok = pst.nextToken();

        if (tok != pst.TT_WORD) throw new IOException("Missing var name in LET");

        String var_name = pst.sval;

        if (pst.nextToken() == pst.TT_EOF) throw new IOException("Missing value in LET");

        pst.pushBack();

        String value = evalLet(pst);

        if (DEBUG) System.out.println("doing let with varname " + var_name + " and value=\"" + value + "\"");

        for (int j = 1; j < region.size(); j++) {

            try {

                String currentLine = (String) region.elementAt(j);

                String result = substitute(currentLine, var_name, value);

                out.print(result + "\n");

            } catch (ClassCastException e) {

                Vector oldRegion = (Vector) region.elementAt(j);

                Vector newRegion = substituteInRegion(oldRegion, var_name, value);

                processTemplateRegion(newRegion);

            }

        }

    }
