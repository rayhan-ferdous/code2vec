    public int read() throws IOException {

        counter++;

        ByteBuffer buf = ByteBuffer.allocate(1);

        if (channel.read(buf) == -1) return -1;

        return buf.get(0) & 0xff;

    }
