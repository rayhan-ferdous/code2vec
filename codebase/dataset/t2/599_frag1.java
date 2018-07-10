        setCookies(cookies);

        setParameters(parameters);

        return post();

    }



    /**

	 * posts the requests to the server, with all the cookies and parameters

	 * that were added before (if any), and with cookies and parameters that are

	 * passed in the arguments

	 * 

	 * @param cookies

	 *            request cookies

	 * @param parameters

	 *            request parameters

	 * @return input stream with the server response

	 * @throws IOException

	 * @see #setParameters

	 * @see setCookies

	 */

    public InputStream post(String[] cookies, Object[] parameters) throws IOException {

        setCookies(cookies);

        setParameters(parameters);

        return post();

    }



    /**

	 * post the POST request to the server, with the specified parameter

	 * 

	 * @param name

	 *            parameter name

	 * @param value

	 *            parameter value

	 * @return input stream with the server response

	 * @throws IOException

	 * @see #setParameter

	 */

    public InputStream post(String name, Object value) throws IOException {

        setParameter(name, value);

        return post();

    }



    /**

	 * post the POST request to the server, with the specified parameters

	 * 

	 * @param name1

	 *            first parameter name

	 * @param value1

	 *            first parameter value

	 * @param name2

	 *            second parameter name

	 * @param value2

	 *            second parameter value

	 * @return input stream with the server response

	 * @throws IOException

	 * @see #setParameter

	 */

    public InputStream post(String name1, Object value1, String name2, Object value2) throws IOException {

        setParameter(name1, value1);

        return post(name2, value2);

    }



    /**

	 * post the POST request to the server, with the specified parameters

	 * 

	 * @param name1

	 *            first parameter name

	 * @param value1

	 *            first parameter value

	 * @param name2

	 *            second parameter name

	 * @param value2

	 *            second parameter value

	 * @param name3

	 *            third parameter name

	 * @param value3

	 *            third parameter value

	 * @return input stream with the server response

	 * @throws IOException

	 * @see #setParameter

	 */

    public InputStream post(String name1, Object value1, String name2, Object value2, String name3, Object value3) throws IOException {

        setParameter(name1, value1);

        return post(name2, value2, name3, value3);

    }



    /**

	 * post the POST request to the server, with the specified parameters

	 * 

	 * @param name1

	 *            first parameter name

	 * @param value1

	 *            first parameter value

	 * @param name2

	 *            second parameter name

	 * @param value2

	 *            second parameter value

	 * @param name3

	 *            third parameter name

	 * @param value3

	 *            third parameter value

	 * @param name4

	 *            fourth parameter name

	 * @param value4

	 *            fourth parameter value

	 * @return input stream with the server response

	 * @throws IOException

	 * @see #setParameter

	 */

    public InputStream post(String name1, Object value1, String name2, Object value2, String name3, Object value3, String name4, Object value4) throws IOException {

        setParameter(name1, value1);

        return post(name2, value2, name3, value3, name4, value4);

    }



    /**

	 * posts a new request to specified URL, with parameters that are passed in

	 * the argument

	 * 

	 * @param parameters

	 *            request parameters

	 * @return input stream with the server response

	 * @throws IOException

	 * @see #setParameters

	 */

    public InputStream post(URL url, Map parameters) throws IOException {

        return new HTTPRequest(url).post(parameters);

    }



    /**

	 * posts a new request to specified URL, with parameters that are passed in

	 * the argument

	 * 

	 * @param parameters

	 *            request parameters

	 * @return input stream with the server response

	 * @throws IOException

	 * @see #setParameters

	 */

    public InputStream post(URL url, Object[] parameters) throws IOException {

        return new HTTPRequest(url).post(parameters);
