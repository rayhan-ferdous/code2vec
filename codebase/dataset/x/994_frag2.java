            setParameter(entry.getKey().toString(), entry.getValue());

        }

    }



    /**

   * adds parameters to the request

   * @param parameters array of parameter names and values (parameters[2*i] is a name, parameters[2*i + 1] is a value); if a value is a file, the file is uploaded, otherwise it is stringified and sent in the request

   * @throws IOException

   */

    public void setParameters(Object[] parameters) throws IOException {

        if (parameters == null) return;

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
