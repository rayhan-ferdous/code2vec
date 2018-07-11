    void processCountRegion(Vector region) throws IOException {

        if (DEBUG) System.out.println("params=\"" + params + "\"");

        QuotedStringTokenizer pst = new QuotedStringTokenizer(params);

        if (!pst.hasMoreTokens()) throw new IOException("Missing var name in COUNT");

        String var_name = pst.nextToken();

        int count = 0;

        while (pst.hasMoreTokens()) {

            String v_i = pst.nextToken();

            int dotdot = v_i.indexOf("..");

            if (dotdot != -1 && dotdot == v_i.lastIndexOf("..")) {

                int start = Integer.parseInt(v_i.substring(0, dotdot));

                int end = Integer.parseInt(v_i.substring(dotdot + 2));

                for (int j = start; j <= end; j++) count++;

            } else count++;

        }

        String value = Integer.toString(count);

        if (DEBUG) System.out.println("doing count with varname " + var_name + " on values :" + params.substring(var_name.length() + 1) + "; count=" + value);

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
