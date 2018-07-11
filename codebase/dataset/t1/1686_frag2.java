    private WritableByteChannel getWriteChannel(String path) throws IOException {

        WritableByteChannel channel;

        File f = new File(path);

        if (!f.exists()) {

            System.out.println("Creando fichero " + f.getAbsolutePath());

            if (!f.createNewFile()) {

                System.err.print("Error al crear el fichero " + f.getAbsolutePath());

                throw new IOException("Cannot create file " + f);

            }

        }

        RandomAccessFile raf = new RandomAccessFile(f, "rw");

        channel = raf.getChannel();

        return channel;

    }



    /**

	 * Use this function first of all, when you need to prepare a writer

	 * having a FLayer as model.

	 * IMPORTANT: Call setFile before calling this function.

	 * @param lyrVect

	 * @throws IOException

	 * @throws DriverException

	 */

    public void initialize(FLayer layer) throws InitializeWriterException {
