        if (!pst.hasMoreTokens()) throw new IOException("Missing variable in FOREACH");

        String var_name = pst.nextToken();

        if (!pst.hasMoreTokens()) throw new IOException("Missing filename in FOREACH");

        String file_name = pst.nextToken();
