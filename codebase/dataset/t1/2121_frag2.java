        ix = readShort();

        iy = readShort();

        iw = readShort();

        ih = readShort();

        int packed = read();

        lctFlag = (packed & 0x80) != 0;

        interlace = (packed & 0x40) != 0;

        lctSize = 2 << (packed & 7);

        if (lctFlag) {

            lct = readColorTable(lctSize);

            act = lct;

        } else {

            act = gct;

            if (bgIndex == transIndex) bgColor = 0;

        }

        int save = 0;

        if (transparency) {

            save = act[transIndex];

            act[transIndex] = 0;

        }

        if (act == null) {

            status = STATUS_FORMAT_ERROR;

        }

        if (err()) return;

        decodeImageData();

        skip();

        if (err()) return;

        frameCount++;

        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);

        setPixels();

        frames.add(new GifFrame(image, delay));

        if (transparency) {

            act[transIndex] = save;

        }

        resetFrame();

    }



    /**

	 * Reads Logical Screen Descriptor

	 */

    protected void readLSD() {
