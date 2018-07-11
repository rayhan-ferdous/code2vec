        if (DEBUG) System.out.println("doing count with varname " + var_name + " on values :" + params.substring(var_name.length() + 1) + "; count=" + value);

        for (int j = 1; j < region.size(); j++) {

            try {

                String currentLine = (String) region.elementAt(j);

                String result = substitute(currentLine, var_name, value);

                out.print(result + "\n");

            } catch (ClassCastException e) {

                Vector oldRegion = (Vector) region.elementAt(j);

                Vector newRegion = substituteInRegion(oldRegion, var_name, value);
