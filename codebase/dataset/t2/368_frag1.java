    private int readInt3(byte bytes[], boolean check_tagLength) {

        if (((index + 2) >= tagLength) && check_tagLength) {

            setError("readInt3(index: " + index + ", tagLength: " + tagLength);

            return -1;

        }

        if ((index + 3) >= bytes.length) {

            setError("readInt3(index: " + index + ", bytes.length: " + bytes.length);

            return -1;

        }

        int array[] = { 0xff & bytes[index++], 0xff & bytes[index++], 0xff & bytes[index++] };

        int result = (array[0] << 16) | (array[1] << 8) | (array[2] << 0);

        return result;

    }
