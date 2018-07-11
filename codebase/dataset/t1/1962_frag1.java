    private void pushBack(int acceptLength) throws IOException {

        int length = this.text.length();

        for (int i = length - 1; i >= acceptLength; i--) {

            this.eof = false;

            this.in.unread(this.text.charAt(i));

        }

    }
