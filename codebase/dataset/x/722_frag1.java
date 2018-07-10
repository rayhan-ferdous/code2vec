    public OutputStream getTailStream() throws FileNotFoundException {

        if (tailStream == null) {

            tailStream = new FlipFileOutputStream();

        }

        return tailStream;

    }
