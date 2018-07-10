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



        public static final State NORMAL = new State(0);



        public static final State PACKAGE = new State(1);



        private int id;



        private State(@SuppressWarnings("hiding") int id) {

            this.id = id;

        }



        public int id() {

            return this.id;

        }

    }



    static {

        try {

            DataInputStream s = new DataInputStream(new BufferedInputStream(Lexer.class.getResourceAsStream("lexer.dat")));

            int length = s.readInt();

            gotoTable = new int[length][][][];

            for (int i = 0; i < gotoTable.length; i++) {

                length = s.readInt();

                gotoTable[i] = new int[length][][];

                for (int j = 0; j < gotoTable[i].length; j++) {

                    length = s.readInt();

                    gotoTable[i][j] = new int[length][3];

                    for (int k = 0; k < gotoTable[i][j].length; k++) {

                        for (int l = 0; l < 3; l++) {

                            gotoTable[i][j][k][l] = s.readInt();

                        }

                    }

                }

            }

            length = s.readInt();

            accept = new int[length][];

            for (int i = 0; i < accept.length; i++) {

                length = s.readInt();

                accept[i] = new int[length];

                for (int j = 0; j < accept[i].length; j++) {

                    accept[i][j] = s.readInt();

                }

            }

            s.close();

        } catch (Exception e) {

            throw new RuntimeException("The file \"lexer.dat\" is either missing or corrupted.");

        }

    }

}
