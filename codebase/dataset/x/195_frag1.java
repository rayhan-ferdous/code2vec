        public RequestThread(Socket s) {

            clientSocket = s;

        }



        /**

		 * Description

		 */

        public void run() {

            try {

                HTTPrequest req = getRequest(clientSocket);

                implementMethod(req);

            } catch (IOException ioe) {

                Debug.output(3, ioe);

            }

        }

    }



    /**

	 * Constructor. Accept an array of parameters, to set the port, backlog and document root.

	 * Creation date: (03/08/2001 12:24:50)

	 * 

	 * @param args java.lang.String[]

	 */

    public WebServer(String[] args) {
