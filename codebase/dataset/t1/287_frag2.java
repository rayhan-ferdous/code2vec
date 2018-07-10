                java.net.URL url = new java.net.URL(ResourceBundle.getBundle("Administration").getString("ServerURL") + ResourceBundle.getBundle("Administration").getString("ServletSubPath") + "SeriesNameServlet");

                java.net.URLConnection urlconn = (java.net.URLConnection) url.openConnection();

                urlconn.setDoOutput(true);

                java.io.OutputStream dos = urlconn.getOutputStream();

                dos.write(xmlreq.getBytes());

                java.io.InputStream ios = urlconn.getInputStream();

                SAXBuilder saxb = new SAXBuilder();

                Document retdoc = saxb.build(ios);

                Element rootelement = retdoc.getRootElement();
