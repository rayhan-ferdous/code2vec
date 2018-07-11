    void processJoinRegion(Vector region) throws IOException {

        if (DEBUG) System.out.println("params=\"" + params + "\"");

        QuotedStringTokenizer pst = new QuotedStringTokenizer(params);

        if (!pst.hasMoreTokens()) throw new IOException("Missing var name in JOIN");

        String var_name = pst.nextToken();

        if (!pst.hasMoreTokens()) throw new IOException("Missing separators in JOIN");

        String sep = pst.nextToken();

        int numValues = pst.countTokens();

        String value = "";

        if (pst.hasMoreTokens()) {

            value = pst.nextToken();

            for (int i = 1; i < numValues; i++) value += sep + pst.nextToken();

        }

        if (DEBUG) System.out.println("doing join with varname " + var_name + " and value=\"" + value + "\"");

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
