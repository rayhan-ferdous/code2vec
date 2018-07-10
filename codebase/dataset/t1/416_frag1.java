        colorTab = nq.process();

        for (int i = 0; i < colorTab.length; i += 3) {

            byte temp = colorTab[i];

            colorTab[i] = colorTab[i + 2];

            colorTab[i + 2] = temp;

            usedEntry[i / 3] = false;

        }

        int k = 0;

        for (int i = 0; i < nPix; i++) {

            int index = nq.map(pixels[k++] & 0xff, pixels[k++] & 0xff, pixels[k++] & 0xff);

            usedEntry[index] = true;

            indexedPixels[i] = (byte) index;

        }
