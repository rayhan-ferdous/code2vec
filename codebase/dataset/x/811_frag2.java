    private long readLong4(long offset) {

        long ret = 0;

        try {

            ipFile.seek(offset);

            ret |= (ipFile.readByte() & 0xFF);

            ret |= ((ipFile.readByte() << 8) & 0xFF00);

            ret |= ((ipFile.readByte() << 16) & 0xFF0000);

            ret |= ((ipFile.readByte() << 24) & 0xFF000000);

            return ret;

        } catch (IOException e) {

            return -1;

        }

    }
