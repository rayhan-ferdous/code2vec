        public void handle(final HttpRequest request, final HttpResponse response, final HttpContext context) throws HttpException, IOException {

            ProtocolVersion ver = request.getRequestLine().getProtocolVersion();

            String uri = request.getRequestLine().getUri();

            if (uri.equals("/oldlocation/")) {

                response.setStatusLine(ver, HttpStatus.SC_MOVED_TEMPORARILY);

                response.addHeader(new BasicHeader("Location", url));

            } else if (uri.equals("/relativelocation/")) {

                response.setStatusLine(ver, HttpStatus.SC_OK);

                StringEntity entity = new StringEntity("Successful redirect");

                response.setEntity(entity);

            } else {

                response.setStatusLine(ver, HttpStatus.SC_NOT_FOUND);

            }

        }
