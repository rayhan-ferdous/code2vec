        if (!pst.hasMoreTokens()) throw new IOException("Missing separators in SPLIT");

        String sep = pst.nextToken();

        if (!pst.hasMoreTokens()) throw new IOException("Missing variables in SPLIT");

        int numVars = pst.countTokens();
