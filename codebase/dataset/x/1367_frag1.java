    public byte[] read(int len) throws EoflvException {

        byte[] buf = new byte[len];

        if (len > data.remaining()) {

            written = data.remaining();

            data.get(buf, 0, written);

            fillBuffer();

            data.get(buf, written, (len - written));

        } else {

            data.get(buf);

        }

        pos += len;

        return buf;

    }
