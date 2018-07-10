        public void run() {

            try {

                InputStream is = mySocket.getInputStream();

                if (is == null) return;

                BufferedReader in = new BufferedReader(new InputStreamReader(is));

                StringTokenizer st = new StringTokenizer(in.readLine());

                if (!st.hasMoreTokens()) sendError(HTTP_BADREQUEST, "BAD REQUEST: Syntax error. Usage: GET /example/file.html");

                String method = st.nextToken();

                if (!st.hasMoreTokens()) sendError(HTTP_BADREQUEST, "BAD REQUEST: Missing URI. Usage: GET /example/file.html");

                String uri = st.nextToken();

                Properties parms = new Properties();

                int qmi = uri.indexOf('?');

                if (qmi >= 0) {

                    decodeParms(uri.substring(qmi + 1), parms);

                    uri = decodePercent(uri.substring(0, qmi));

                } else uri = decodePercent(uri);

                Properties header = new Properties();

                if (st.hasMoreTokens()) {

                    String line = in.readLine();

                    while (line.trim().length() > 0) {

                        int p = line.indexOf(':');

                        header.put(line.substring(0, p).trim().toLowerCase(), line.substring(p + 1).trim());

                        line = in.readLine();

                    }

                }

                if (method.equalsIgnoreCase("POST")) {

                    long size = 0x7FFFFFFFFFFFFFFFl;

                    String contentLength = header.getProperty("content-length");

                    if (contentLength != null) {

                        try {

                            size = Integer.parseInt(contentLength);

                        } catch (NumberFormatException ex) {

                        }

                    }

                    String postLine = "";

                    char buf[] = new char[512];

                    int read = in.read(buf);

                    while (read >= 0 && size > 0 && !postLine.endsWith("\r\n")) {

                        size -= read;

                        postLine += String.valueOf(buf, 0, read);

                        if (size > 0) read = in.read(buf);

                    }

                    postLine = postLine.trim();

                    decodeParms(postLine, parms);

                }

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
