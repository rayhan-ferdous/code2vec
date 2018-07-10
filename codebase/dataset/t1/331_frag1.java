    public void setParameter(String name, String filename, InputStream is) throws IOException {

        boundary();

        writeName(name);

        write("; filename=\"");

        write(filename);

        write('"');

        newline();

        write("Content-Type: ");

        String type = URLConnection.guessContentTypeFromName(filename);

        if (type == null) type = "application/octet-stream";

        writeln(type);

        newline();

        pipe(is, os);

        newline();

    }
