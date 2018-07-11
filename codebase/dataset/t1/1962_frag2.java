    private void pushBack(int acceptLength) throws IOException {

        int length = text.length();

        for (int i = length - 1; i >= acceptLength; i--) {

            eof = false;

            in.unread(text.charAt(i));

        }

    }
