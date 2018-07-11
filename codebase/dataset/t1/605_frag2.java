        data_size = read();

        clear = 1 << data_size;

        end_of_information = clear + 1;

        available = clear + 2;

        old_code = NullCode;

        code_size = data_size + 1;

        code_mask = (1 << code_size) - 1;

        for (code = 0; code < clear; code++) {

            prefix[code] = 0;

            suffix[code] = (byte) code;

        }

        datum = bits = count = first = top = pi = bi = 0;

        for (i = 0; i < npix; ) {

            if (top == 0) {

                if (bits < code_size) {

                    if (count == 0) {

                        count = readBlock();

                        if (count <= 0) break;

                        bi = 0;

                    }

                    datum += (((int) block[bi]) & 0xff) << bits;

                    bits += 8;

                    bi++;

                    count--;

                    continue;

                }

                code = datum & code_mask;

                datum >>= code_size;

                bits -= code_size;

                if ((code > available) || (code == end_of_information)) break;

                if (code == clear) {

                    code_size = data_size + 1;

                    code_mask = (1 << code_size) - 1;

                    available = clear + 2;

                    old_code = NullCode;

                    continue;

                }

                if (old_code == NullCode) {

                    pixelStack[top++] = suffix[code];

                    old_code = code;

                    first = code;

                    continue;

                }

                in_code = code;

                if (code == available) {

                    pixelStack[top++] = (byte) first;

                    code = old_code;

                }

                while (code > clear) {

                    pixelStack[top++] = suffix[code];

                    code = prefix[code];

                }

                first = ((int) suffix[code]) & 0xff;

                if (available >= MaxStackSize) break;

                pixelStack[top++] = (byte) first;

                prefix[available] = (short) old_code;

                suffix[available] = (byte) first;

                available++;

                if (((available & code_mask) == 0) && (available < MaxStackSize)) {

                    code_size++;

                    code_mask += available;

                }

                old_code = in_code;

            }

            top--;
