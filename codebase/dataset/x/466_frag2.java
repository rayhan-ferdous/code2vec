    private static final int hexToInt(char c) {

        if ((c >= 'a') && (c <= 'f')) {

            return (c - 'a') + 10;

        }

        if ((c >= 'A') && (c <= 'F')) {

            return (c - 'A') + 10;

        }

        if ((c >= '0') && (c <= '9')) {

            return (c - '0');

        }

        throw new IllegalArgumentException("Need hex char");

    }
