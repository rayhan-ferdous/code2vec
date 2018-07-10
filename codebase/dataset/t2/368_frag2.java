    private int readInt(byte bytes[], boolean check_tagLength) {

        if (((index + 3) >= tagLength) && check_tagLength) {

            setError("readInt(index: " + index + ", tagLength: " + tagLength);

            return -1;

        }

        if ((index + 3) >= bytes.length) {

            setError("readInt(index: " + index + ", bytes.length: " + bytes.length);

            return -1;

        }

        int array[] = { 0xff & bytes[index++], 0xff & bytes[index++], 0xff & bytes[index++], 0xff & bytes[index++] };

        int result = (array[0] << 24) | (array[1] << 16) | (array[2] << 8) | (array[3] << 0);

        return result;

    }
