    public Token next() throws LexerException, IOException {

        while (this.token == null) {

            this.token = getToken();

            filter();

        }

        Token result = this.token;

        this.token = null;

        return result;

    }
