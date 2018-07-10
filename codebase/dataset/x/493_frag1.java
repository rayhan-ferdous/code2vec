        if (test.equals("all") || test.equals("exampleContentHandlerToContentHandler")) {

            System.out.println("\n\n==== exampleContentHandlerToContentHandler ====");

            try {

                exampleContentHandlerToContentHandler(foo_xml, foo_xsl);

            } catch (Exception ex) {

                handleException(ex);

            }

        }

        if (test.equals("all") || test.equals("exampleXMLReader")) {
