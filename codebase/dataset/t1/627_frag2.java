    void processJoinRegion(Vector region) throws IOException {

        if (DEBUG) System.out.println("params=\"" + params + "\"");

        QuotedStringTokenizer pst = new QuotedStringTokenizer(params);

        if (!pst.hasMoreTokens()) throw new IOException("Missing var name in JOIN");

        String var_name = pst.nextToken();

        if (!pst.hasMoreTokens()) throw new IOException("Missing separators in JOIN");
