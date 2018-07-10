        public void run() {

            try {

                InputStream is = mySocket.getInputStream();

                if (is == null) return;

                BufferedReader in = new BufferedReader(new InputStreamReader(is));

                final String firstLine = in.readLine();

                if (firstLine == null) return;

                StringTokenizer st = new StringTokenizer(firstLine);

                if (!st.hasMoreTokens()) sendError(HTTP_BADREQUEST, "BAD REQUEST: Syntax error. Usage: GET /example/file.html");

                String method = st.nextToken();

                if (!st.hasMoreTokens()) sendError(HTTP_BADREQUEST, "BAD REQUEST: Missing URI. Usage: GET /example/file.html");

                String uri = decodePercent(st.nextToken());

                Properties parms = new Properties();

                int qmi = uri.indexOf('?');

                if (qmi >= 0) {

                    decodeParms(uri.substring(qmi + 1), parms);

                    uri = decodePercent(uri.substring(0, qmi));

                }

                Properties header = new Properties();

                if (st.hasMoreTokens()) {

                    String line = in.readLine();

                    while (line.trim().length() > 0) {

                        int p = line.indexOf(':');

                        header.put(line.substring(0, p).trim(), line.substring(p + 1).trim());

                        line = in.readLine();

                    }

                }

                if (method.equalsIgnoreCase("POST")) decodeParms(in.readLine(), parms);

                Response r = serve(uri, method, header, parms);

                if (r == null) sendError(HTTP_INTERNALERROR, "SERVER INTERNAL ERROR: Serve() returned a null response."); else sendResponse(r.status, r.mimeType, r.header, r.data);

                in.close();

            } catch (IOException ioe) {

                try {

                    sendError(HTTP_INTERNALERROR, "SERVER INTERNAL ERROR: IOException: " + ioe.getMessage());

                } catch (Throwable t) {

                }

            } catch (InterruptedException ie) {

            }

        }
