        int total = 0;

        synchronized (in) {

            while ((nread = in.read(buf, 0, buf.length)) >= 0) {

                out.write(buf, 0, nread);

                total += nread;

            }

        }

        out.flush();

        buf = null;

    }



    /**

     * adds a file parameter to the request

     * 

     * @param name

     *            parameter name

     * @param filename

     *            the name of the file

     * @param is

     *            input stream to read the contents of the file from

     * @throws IOException

     */

    public void setParameter(String name, String filename, InputStream is) throws IOException {

        boundary();

        writeName(name);

        write("; filename=\"");

        write(filename);

        write('"');

        newline();

        write("Content-Type: ");

        String type = URLConnection.guessContentTypeFromName(filename);

        if (type == null) type = "application/octet-stream";

        writeln(type);

        newline();

        pipe(is, os);

        newline();

    }



    /**

     * adds a file parameter to the request

     * 

     * @param name

     *            parameter name

     * @param file

     *            the file to upload

     * @throws IOException

     */

    public void setParameter(String name, File file) throws IOException {

        setParameter(name, file.getPath(), new FileInputStream(file));

    }



    /**

     * adds a parameter to the request; if the parameter is a File, the file is

     * uploaded, otherwise the string value of the parameter is passed in the

     * request

     * 

     * @param name

     *            parameter name

     * @param object

     *            parameter value, a File or anything else that can be

     *            stringified

     * @throws IOException

     */

    public void setParameter(String name, Object object) throws IOException {

        if (object instanceof File) {

            setParameter(name, (File) object);

        } else {

            setParameter(name, object.toString());

        }

    }



    /**

     * adds parameters to the request

     * 

     * @param parameters

     *            "name-to-value" map of parameters; if a value is a file, the

     *            file is uploaded, otherwise it is stringified and sent in the

     *            request

     * @throws IOException

     */

    public void setParameters(Map<Object, Object> parameters) throws IOException {
