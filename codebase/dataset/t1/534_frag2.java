        for (int i = 0; i < parameters.length - 1; i += 2) {

            setParameter(parameters[i].toString(), parameters[i + 1]);

        }

    }



    /**

   * posts the requests to the server, with all the cookies and parameters that were added

   * @return input stream with the server response

   * @throws IOException

   */

    public InputStream post() throws IOException {

        boundary();

        writeln("--");

        os.close();

        return connection.getInputStream();

    }



    /**

   * posts the requests to the server, with all the cookies and parameters that were added before (if any), and with parameters that are passed in the argument

   * @param parameters request parameters

   * @return input stream with the server response

   * @throws IOException

   * @see setParameters

   */

    public InputStream post(Map parameters) throws IOException {

        setParameters(parameters);

        return post();

    }



    /**

   * posts the requests to the server, with all the cookies and parameters that were added before (if any), and with parameters that are passed in the argument

   * @param parameters request parameters

   * @return input stream with the server response

   * @throws IOException

   * @see setParameters

   */

    public InputStream post(Object[] parameters) throws IOException {

        setParameters(parameters);

        return post();
