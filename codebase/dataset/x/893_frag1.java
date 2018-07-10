    protected int[] readColorTable(int ncolors) {

        int nbytes = 3 * ncolors;

        int[] tab = null;

        byte[] c = new byte[nbytes];

        int n = 0;

        try {

            n = in.read(c);

        } catch (IOException e) {

        }

        if (n < nbytes) status = STATUS_FORMAT_ERROR; else {

            tab = new int[256];

            int i = 0;

            int j = 0;

            while (i < ncolors) {

                int r = ((int) c[j++]) & 0xff;

                int g = ((int) c[j++]) & 0xff;

                int b = ((int) c[j++]) & 0xff;

                tab[i++] = 0xff000000 | (r << 16) | (g << 8) | b;

            }

        }

        return tab;

    }



    /**

     * Main file parser.  Reads GIF content blocks.

     */

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

                            for (int i = 0; i < 11; i++) app += (char) block[i];

                            if (app.equals("NETSCAPE2.0")) readNetscapeExt(); else skip();

                            break;

                        default:

                            skip();

                    }

                    break;

                case 0x3b:

                    done = true;

                    break;

                default:
