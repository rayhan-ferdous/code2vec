    public double readDouble() throws IOException {

        byte[] bytes = read(8);

        byte[] bytes2 = new byte[8];

        bytes2[0] = bytes[3];

        bytes2[1] = bytes[2];

        bytes2[2] = bytes[1];

        bytes2[3] = bytes[0];

        bytes2[4] = bytes[7];

        bytes2[5] = bytes[6];

        bytes2[6] = bytes[5];

        bytes2[7] = bytes[4];

        ByteArrayInputStream bin = new ByteArrayInputStream(bytes2);

        return new DataInputStream(bin).readDouble();

    }
