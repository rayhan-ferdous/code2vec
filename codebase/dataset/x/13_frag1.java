    String sequenceUsingDo(int start, int stop) {

        StringBuilder builder = new StringBuilder();

        int i = start;

        do {

            if (i > start) builder.append(',');

            builder.append(i);

        } while (++i <= stop);

        return builder.toString();

    }
