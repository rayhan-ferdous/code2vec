    public static String readFully(File file, Charset charset) throws IOException {

        Reader reader = new InputStreamReader(new FileInputStream(file), charset);

        String s = readFully(reader);

        reader.close();

        return s;

    }
