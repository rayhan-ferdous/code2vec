    String sequenceUsingFor(int start, int stop) {

        StringBuilder builder = new StringBuilder();

        for (int i = start; i <= stop; i++) {

            if (i > start) builder.append(',');

            builder.append(i);

        }

        return builder.toString();

    }
