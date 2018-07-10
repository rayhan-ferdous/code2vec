class CopyThread extends Thread {



    private File dest;



    private String filename;



    /**

	 * Default constructor

	 * @param dest a file descriptor to the destination

	 * @param filename the name of the resource

	 */

    public CopyThread(File dest, String filename) {

        this.dest = dest;

        this.filename = filename;

    }



    public void run() {

        try {

            InputStream is = Statics.class.getResourceAsStream(filename);

            FileOutputStream fos = new FileOutputStream(dest);
