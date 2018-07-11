    Token new11(@SuppressWarnings("hiding") String text, @SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) {

        return new TNewLine(text, line, pos);

    }



    Token new12(@SuppressWarnings("hiding") String text, @SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) {

        return new TBlank(text, line, pos);

    }



    private int getChar() throws IOException {
