    public void postCommand(Map parameters) throws IOException {

        setParameters(parameters);

        PrintWriter out = new PrintWriter(connection.getOutputStream());

        out.print("");

        out.flush();

        StringBuffer result = new StringBuffer();

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String inputLine;

        while ((inputLine = in.readLine()) != null) {

            result.append(inputLine);

        }

        in.close();

        out.close();

        connection.disconnect();

    }
