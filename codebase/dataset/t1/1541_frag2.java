    static byte[] encode3to4(byte[] source, int srcOffset, int numSigBytes, byte[] destination, int destOffset) {

        int inBuff = (numSigBytes > 0 ? source[srcOffset] << 24 >>> 8 : 0) | (numSigBytes > 1 ? source[srcOffset + 1] << 24 >>> 16 : 0) | (numSigBytes > 2 ? source[srcOffset + 2] << 24 >>> 24 : 0);

        switch(numSigBytes) {

            case 3:

                destination[destOffset] = ALPHABET[(inBuff >>> 18)];

                destination[destOffset + 1] = ALPHABET[inBuff >>> 12 & 0x3f];

                destination[destOffset + 2] = ALPHABET[inBuff >>> 6 & 0x3f];

                destination[destOffset + 3] = ALPHABET[inBuff & 0x3f];

                return destination;

            case 2:

                destination[destOffset] = ALPHABET[(inBuff >>> 18)];

                destination[destOffset + 1] = ALPHABET[inBuff >>> 12 & 0x3f];

                destination[destOffset + 2] = ALPHABET[inBuff >>> 6 & 0x3f];

                destination[destOffset + 3] = EQUALS_SIGN;

                return destination;

            case 1:

                destination[destOffset] = ALPHABET[(inBuff >>> 18)];

                destination[destOffset + 1] = ALPHABET[inBuff >>> 12 & 0x3f];

                destination[destOffset + 2] = EQUALS_SIGN;

                destination[destOffset + 3] = EQUALS_SIGN;

                return destination;

            default:

                return destination;

        }

    }
