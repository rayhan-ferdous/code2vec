    private boolean evalCond(StreamTokenizer st) throws IOException {

        int val = evalExpr(st);

        int token = st.nextToken();

        switch(token) {

            case '>':

                if (st.nextToken() == '=') return val >= evalExpr(st); else {

                    st.pushBack();

                    return val > evalExpr(st);

                }

            case '<':

                if (st.nextToken() == '=') return val <= evalExpr(st); else {

                    st.pushBack();

                    return val < evalExpr(st);

                }

            case '=':

                if (st.nextToken() != '=') throw new IOException("Invalid token");

                return val == evalExpr(st);

            case '!':

                if (st.nextToken() != '=') throw new IOException("Invalid token");

                return val != evalExpr(st);

            default:

                throw new IOException("Invalid token");

        }

    }
