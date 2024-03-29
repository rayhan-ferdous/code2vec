    protected boolean err() {

        return status != STATUS_OK;

    }



    /**

	 * Initializes or re-initializes reader.

	 */

    protected void init() {

        status = STATUS_OK;

        frameCount = 0;

        frames = new ArrayList();

        gct = null;

        lct = null;

    }



    /**

	 * Reads a single byte from the input stream.

	 * @return an int representing the read in byte value

	 */

    protected int read() {

        int curByte = 0;

        try {

            curByte = in.read();

        } catch (IOException e) {

            status = STATUS_FORMAT_ERROR;

        }

        return curByte;

    }



    /**

	 * Reads next variable length block from input.

	 *

	 * @return number of bytes stored in "buffer"

	 */

    protected int readBlock() {

        blockSize = read();

        int n = 0;

        if (blockSize > 0) {

            try {

                int count = 0;

                while (n < blockSize) {

                    count = in.read(block, n, blockSize - n);

                    if (count == -1) break;

                    n += count;

                }

            } catch (IOException e) {
