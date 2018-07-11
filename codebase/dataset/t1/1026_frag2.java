    String sequenceUsingWhile(int start, int stop) {

        StringBuilder builder = new StringBuilder();

        int i = start;

        while (i <= stop) {

            if (i > start) builder.append(',');

            builder.append(i);

            i++;

        }

        return builder.toString();

    }
