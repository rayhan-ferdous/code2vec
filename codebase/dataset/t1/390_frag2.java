        boundary();

        writeName(name);

        write("; filename=\"");

        write(filename);

        write('"');

        newline();

        write("Content-Type: ");

        String type = connection.guessContentTypeFromName(filename);
