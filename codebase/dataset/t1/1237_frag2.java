    public static Matrix read(BufferedReader input) throws java.io.IOException {

        StreamTokenizer tokenizer = new StreamTokenizer(input);

        tokenizer.resetSyntax();

        tokenizer.wordChars(0, 255);

        tokenizer.whitespaceChars(0, ' ');

        tokenizer.eolIsSignificant(true);

        java.util.Vector v = new java.util.Vector();

        while (tokenizer.nextToken() == StreamTokenizer.TT_EOL) ;

        if (tokenizer.ttype == StreamTokenizer.TT_EOF) throw new java.io.IOException("Unexpected EOF on matrix read.");

        do {

            v.addElement(Double.valueOf(tokenizer.sval));

        } while (tokenizer.nextToken() == StreamTokenizer.TT_WORD);

        int n = v.size();

        double row[] = new double[n];

        for (int j = 0; j < n; j++) row[j] = ((Double) v.elementAt(j)).doubleValue();
