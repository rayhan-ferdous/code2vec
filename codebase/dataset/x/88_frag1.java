        if (DEBUG) System.out.println("params=\"" + params + "\"");

        QuotedStringTokenizer pst = new QuotedStringTokenizer(params);

        if (!pst.hasMoreTokens()) throw new IOException("Missing value in SPLIT");

        String value = pst.nextToken();

        if (!pst.hasMoreTokens()) throw new IOException("Missing separators in SPLIT");
