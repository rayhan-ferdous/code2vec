                current = nextNBits(9);

                entry = black[current];

                isT = entry & 0x0001;

                bits = (entry >>> 1) & 0x000f;

                code = (entry >>> 5) & 0x07ff;

                if (bits == 12) {

                    updatePointer(5);

                    current = nextLesserThan8Bits(4);

                    entry = additionalMakeup[current];

                    bits = (entry >>> 1) & 0x07;

                    code = (entry >>> 4) & 0x0fff;

                    runLength += code;

                    updatePointer(4 - bits);

                } else if (bits == 15) {
