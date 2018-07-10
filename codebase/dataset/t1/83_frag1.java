            if (position >= 0) {

                if (position >= numSigBytes) return -1;

                if (encode && breakLines && lineLength >= MAX_LINE_LENGTH) {

                    lineLength = 0;

                    return '\n';

                } else {

                    lineLength++;

                    int b = buffer[position++];

                    if (position >= bufferLength) position = -1;

                    return b & 0xFF;

                }

            } else {
