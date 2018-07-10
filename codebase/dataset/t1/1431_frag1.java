    protected void readContents() {

        boolean done = false;

        while (!(done || err())) {

            int code = read();

            switch(code) {

                case 0x2C:

                    readImage();

                    break;

                case 0x21:

                    code = read();

                    switch(code) {

                        case 0xf9:

                            readGraphicControlExt();

                            break;

                        case 0xff:

                            readBlock();

                            String app = "";

                            for (int i = 0; i < 11; i++) {

                                app += (char) block[i];

                            }

                            if (app.equals("NETSCAPE2.0")) {

                                readNetscapeExt();

                            } else skip();

                            break;

                        default:

                            skip();

                    }

                    break;

                case 0x3b:

                    done = true;

                    break;

                case 0x00:

                    break;

                default:

                    status = STATUS_FORMAT_ERROR;

            }

        }

    }



    /**

     * Reads Graphics Control Extension values

     */

    protected void readGraphicControlExt() {

        read();

        int packed = read();

        dispose = (packed & 0x1c) >> 2;

        if (dispose == 0) {

            dispose = 1;

        }

        transparency = (packed & 1) != 0;

        delay = readShort() * 10;

        transIndex = read();

        read();

    }



    /**

     * Reads GIF file header information.

     */

    protected void readHeader() {

        String id = "";

        for (int i = 0; i < 6; i++) {

            id += (char) read();

        }

        if (!id.startsWith("GIF")) {

            status = STATUS_FORMAT_ERROR;

            return;

        }

        readLSD();

        if (gctFlag && !err()) {

            gct = readColorTable(gctSize);

            bgColor = gct[bgIndex];

        }

    }



    /**

     * Reads next frame image

     */

    protected void readImage() {

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

        width = readShort();

        height = readShort();

        int packed = read();

        gctFlag = (packed & 0x80) != 0;

        gctSize = 2 << (packed & 7);

        bgIndex = read();

        pixelAspect = read();

    }



    /**

     * Reads Netscape extenstion to obtain iteration count

     */

    protected void readNetscapeExt() {
