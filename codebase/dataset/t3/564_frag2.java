    private int getChar() throws IOException {

        if (this.eof) {

            return -1;

        }

        int result = this.in.read();

        if (result == -1) {

            this.eof = true;

        }

        return result;

    }



    private void pushBack(int acceptLength) throws IOException {

        int length = this.text.length();

        for (int i = length - 1; i >= acceptLength; i--) {

            this.eof = false;

            this.in.unread(this.text.charAt(i));

        }

    }



    protected void unread(@SuppressWarnings("hiding") Token token) throws IOException {

        @SuppressWarnings("hiding") String text = token.getText();

        int length = text.length();

        for (int i = length - 1; i >= 0; i--) {

            this.eof = false;

            this.in.unread(text.charAt(i));

        }

        this.pos = token.getPos() - 1;

        this.line = token.getLine() - 1;

    }



    private String getText(int acceptLength) {

        StringBuffer s = new StringBuffer(acceptLength);

        for (int i = 0; i < acceptLength; i++) {

            s.append(this.text.charAt(i));

        }

        return s.toString();

    }



    private static int[][][][] gotoTable;



    private static int[][] accept;



    public static class State {



        public static final State START = new State(0);



        public static final State SHORT_OPTION = new State(1);



        public static final State LONG_OPTION = new State(2);



        public static final State OPERAND = new State(3);



        private int id;



        private State(@SuppressWarnings("hiding") int id) {

            this.id = id;

        }



        public int id() {

            return this.id;

        }

    }



    static {
