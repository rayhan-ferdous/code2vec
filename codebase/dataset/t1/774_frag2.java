        if (source[srcOffset + 2] == EQUALS_SIGN) {

            int outBuff = ((DECODABET[source[srcOffset]] & 0xFF) << 18) | ((DECODABET[source[srcOffset + 1]] & 0xFF) << 12);

            destination[destOffset] = (byte) (outBuff >>> 16);

            return 1;

        } else if (source[srcOffset + 3] == EQUALS_SIGN) {

            int outBuff = ((DECODABET[source[srcOffset]] & 0xFF) << 18) | ((DECODABET[source[srcOffset + 1]] & 0xFF) << 12) | ((DECODABET[source[srcOffset + 2]] & 0xFF) << 6);

            destination[destOffset] = (byte) (outBuff >>> 16);

            destination[destOffset + 1] = (byte) (outBuff >>> 8);

            return 2;

        } else {
