    public static String readFully(File file) throws IOException {

        Reader reader = new InputStreamReader(new FileInputStream(file), SYSTEM_CHARSET);

        String s = readFully(reader);

        reader.close();

        return s;

    }
