    public void append(String[] args, int off) throws IOException {

        DirWriter writer = fact.newDirWriter(dirFile, encodeParam());

        try {

            build(writer, args, off);

        } finally {

            writer.close();

        }

    }
