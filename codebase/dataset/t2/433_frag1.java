        long krformat = 0, totalformat = 1;

        float rangeval = 0, formatval = 0;

        int row, column;

        rawtextlen = content.length;

        for (i = 0; i < rawtextlen - 1; i++) {

            if (content[i] >= 0) {

            } else {

                dbchars++;

                if ((byte) 0xA1 <= content[i] && content[i] <= (byte) 0xFE && (byte) 0xA1 <= content[i + 1] && content[i + 1] <= (byte) 0xFE) {

                    krchars++;

                    totalformat += 500;

                    row = content[i] + 256 - 0xA1;

                    column = content[i + 1] + 256 - 0xA1;

                    if (EUC_KRformat[row][column] != 0) {

                        krformat += EUC_KRformat[row][column];

                    } else if (15 <= row && row < 55) {
