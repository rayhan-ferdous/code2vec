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
