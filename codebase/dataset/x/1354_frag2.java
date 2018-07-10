    public void doArabicShapping() {

        int src = 0;

        int dest = 0;

        for (; ; ) {

            while (src < totalTextLength) {

                char c = text[src];

                if (c >= 0x0600 && c <= 0x06ff) break;

                if (src != dest) {

                    text[dest] = text[src];

                    detailChunks[dest] = detailChunks[src];

                    orderLevels[dest] = orderLevels[src];

                }

                ++src;

                ++dest;

            }

            if (src >= totalTextLength) {

                totalTextLength = dest;

                return;

            }

            int startArabicIdx = src;

            ++src;

            while (src < totalTextLength) {

                char c = text[src];

                if (c < 0x0600 || c > 0x06ff) break;

                ++src;

            }

            int arabicWordSize = src - startArabicIdx;

            int size = ArabicLigaturizer.arabic_shape(text, startArabicIdx, arabicWordSize, text, dest, arabicWordSize, arabicOptions);

            if (startArabicIdx != dest) {

                for (int k = 0; k < size; ++k) {

                    detailChunks[dest] = detailChunks[startArabicIdx];

                    orderLevels[dest++] = orderLevels[startArabicIdx++];

                }

            } else dest += size;

        }

    }
