    protected Comment readComment(BufferedReader reader, String line) throws IOException {

        if (line == null) {

            return new Comment("");

        }

        boolean shouldContinue = true;

        StringBuilder sb = new StringBuilder(line);

        String aux = line;

        while (shouldContinue && (aux != null) && aux.endsWith(PropertyMapping.PROPERTY_VALUE_LINE_SEPARATOR)) {

            reader.mark(READ_AHEAD_BUFFER_SIZE);

            aux = reader.readLine();

            if (aux == null) {

                shouldContinue = false;

            } else {

                if (isComment(aux)) {

                    sb.append(LINE_SEPARATOR);

                    sb.append(aux);

                } else {

                    shouldContinue = false;

                    reader.reset();

                }

            }

        }

        return new Comment(sb.toString());

    }
