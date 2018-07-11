    protected void write(char c) throws IOException {

        connect();

        os.write(c);

    }



    protected void write(String s) throws IOException {

        connect();

        os.write(s.getBytes());

    }



    protected void newline() throws IOException {

        connect();

        write("\r\n");

    }



    protected void writeln(String s) throws IOException {

        connect();

        write(s);

        newline();

    }



    private static Random random = new Random();



    protected static String randomString() {

        return Long.toString(random.nextLong(), 36);

    }



    String boundary = "---------------------------" + randomString() + randomString() + randomString();



    private void boundary() throws IOException {

        write("--");

        write(boundary);

    }



    /**

     * Creates a new multipart POST HTTP request on a freshly opened URLConnection

     *

     * @param connection an already open URL connection

     * @throws IOException

     */

    public ClientHttpRequest(URLConnection connection) throws IOException {

        this.connection = connection;

        connection.setDoOutput(true);

        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

    }



    /**

     * Creates a new multipart POST HTTP request for a specified URL

     *

     * @param url the URL to send request to

     * @throws IOException

     */

    public ClientHttpRequest(URL url) throws IOException {
