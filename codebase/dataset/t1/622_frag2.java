    private String getNextLine(BufferedReader data) throws IOException {

        String line = data.readLine();

        if (line == null || line.length() == 0) return line;

        while (line.startsWith(commentMarker)) line = data.readLine();

        while (line.endsWith("\\")) line = line.substring(0, line.length() - 1) + data.readLine();

        return line;

    }
