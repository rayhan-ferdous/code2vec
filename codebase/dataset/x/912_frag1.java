    String read(File f) throws IOException {

        byte[] bytes = new byte[(int) f.length()];

        DataInputStream in = new DataInputStream(new FileInputStream(f));

        try {

            in.readFully(bytes);

        } finally {

            in.close();

        }

        return new String(bytes);

    }
