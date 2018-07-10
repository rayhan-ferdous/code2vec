    private int evalFactor(StreamTokenizer st) throws IOException {

        int tok = st.nextToken();

        switch(tok) {

            case StreamTokenizer.TT_NUMBER:

                return (int) st.nval;

            case '-':

                return -evalFactor(st);

            case '~':

                return ~evalFactor(st);

            case '(':

                int val = evalExpr(st);

                if (st.nextToken() != ')') throw new IOException("Mismatched parentheses");

                return val;

            case StreamTokenizer.TT_WORD:

                if (st.sval.equals("@LENGTH")) return evalLength(st); else throw new IOException("Invalid token");

            default:

                throw new IOException("Invalid token");

        }

    }
