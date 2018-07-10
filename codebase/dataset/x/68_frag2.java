    void write(char c) {

        try {

            writer.write(c);

        } catch (IOException e) {

            throw new WrappedIOException(e);

        }

    }
